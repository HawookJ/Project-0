package com.mealkit.repository;

import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.AdminPostComment;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final AdminPostRepository adminPostRepository;
    private final AdminPostCommentRepository adminPostCommentRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    JpaRepositoryTest(
            @Autowired AdminPostRepository adminPostRepository,
            @Autowired AdminPostCommentRepository adminPostCommentRepository,
            @Autowired UserRepository userRepository,
            @Autowired HashtagRepository hashtagRepository
    ) {
        this.adminPostRepository = adminPostRepository;
        this.adminPostCommentRepository = adminPostCommentRepository;
        this.userRepository = userRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<AdminPost> adminPost = adminPostRepository.findAll();

        // Then
        assertThat(adminPost)
                .isNotNull()
                .hasSize(50); // classpath:resources/data.sql 참조
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = adminPostRepository.count();
        UserAccount userAccount = userRepository.save(UserAccount.of(
                3L,
                "username",
                4,
                "userEmaill",
                "haaakkk",
                "Unochild",
                "nickemae",
                "memo455",
                "naver",
                RoleType.USER
        ));


        AdminPost adminPost = AdminPost.of(
                userAccount,
                "homeName",
                "Address",
                "23434-3434",
                true,
                3490,
                34,
                "homeregggi",
                "youyou",
                "breaklunch",
                34L,
                "setials",
                "titleHOme");
        adminPost.addHashtags(Set.of(Hashtag.of("spring")));

        // When
        adminPostRepository.save(adminPost);

        // Then
        assertThat(adminPostRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        System.out.println("갯수확인 : " +adminPostRepository.count());
        System.out.println("전체 소환!!! : " + adminPostRepository.findAll());
        AdminPost adminPost = adminPostRepository.findById(3L).orElseThrow();
        System.out.println("어디민포스트 리포 업데이트 : " + adminPost);
        Hashtag updatedHashtag = Hashtag.of("springboot");
        adminPost.clearHashtags();
        adminPost.addHashtags(Set.of(updatedHashtag));

        // When
        AdminPost savedAdminPost = adminPostRepository.saveAndFlush(adminPost);

        // Then
        assertThat(savedAdminPost.getHashtags())
                .hasSize(1)
                .extracting("hashtagName", String.class)
                .containsExactly(updatedHashtag.getHashtagName());
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given


        AdminPost adminPost = adminPostRepository.findById(3L).orElseThrow();

        System.out.println("3번쨰 확인 : " + adminPost);
        System.out.println("adminPost + userACcount  재확인 : " + adminPost.getUserAccount().getUserName());
        System.out.println("댓글있나 확인 : " + adminPost.getAdminPostComments());
        System.out.println("댓글있나 확인 : " + adminPostCommentRepository.findByAdminPost_HomeId(adminPost.getHomeId()));

        long previousAdminPostCount = adminPostRepository.count();
        long previousAdminPostCommentCount = adminPostCommentRepository.count();
        int deletedCommentsSize = adminPost.getAdminPostComments().size();

        System.out.println("previousAdminPostCount: " + previousAdminPostCount);
        System.out.println("previousAdminPostCommentCount : " + previousAdminPostCommentCount);
        System.out.println("deletedCommentsSize : " + deletedCommentsSize);

        // When
        adminPostRepository.delete(adminPost);

        // Then
        assertThat(adminPostRepository.count()).isEqualTo(previousAdminPostCount - 1);
        System.out.println((adminPostRepository.count()));
         assertThat(adminPostCommentRepository.count()).isEqualTo(previousAdminPostCommentCount - deletedCommentsSize);
    }


    @DisplayName("대댓글 조회 테스트")
    @Test
    void givenParentCommentId_whenSelecting_thenReturnsChildComments() {
        // Given

        Optional<AdminPostComment> parentComment = adminPostCommentRepository.findById(3L);

        AdminPostComment childComment = AdminPostComment.of(
                parentComment.get().getUserAccount(),
                parentComment.get().getAdminCommentContent(),
                parentComment.get().getAdminPost()
        );

        parentComment.get().addChildComment(childComment);
        adminPostCommentRepository.flush();
        // When


        // Then
        assertThat(parentComment).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @DisplayName("댓글에 대댓글 삽입 테스트")
    @Test
    void givenParentComment_whenSaving_thenInsertsChildComment() {
        // Given
        AdminPostComment parentComment = adminPostCommentRepository.getReferenceById(3L);
        AdminPostComment childComment = AdminPostComment.of(
                parentComment.getUserAccount(),
                parentComment.getAdminCommentContent(),
                parentComment.getAdminPost()
        );

        // When
        parentComment.addChildComment(childComment);
        adminPostCommentRepository.flush();
        parentComment.addChildComment(childComment);
        adminPostCommentRepository.flush();

        // Then
        assertThat(adminPostCommentRepository.findById(3L)).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(2);
    }

    @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트")
    @Test
    void giveAdminPostCommentHavingChildComments_whenDeletingParentComment_thenDeletesEveryComment() {
        // Given
        AdminPostComment parentComment = adminPostCommentRepository.getReferenceById(1L);
        long previousAdminPostCommentCount = adminPostCommentRepository.count();

        // When
        adminPostCommentRepository.delete(parentComment);

        // Then
        assertThat(adminPostCommentRepository.count()).isEqualTo(previousAdminPostCommentCount - 5); // 테스트 댓글 + 대댓글 4개
    }

    @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트 - 댓글 ID + 유저 ID")
    @Test
    void givenAdminPostCommentIdHavingChildCommentsAndUserId_whenDeletingParentComment_thenDeletesEveryComment() {
        // Given
        long previousAdminCommentCount = adminPostCommentRepository.count();

        // When
        adminPostCommentRepository.deleteByAdminPostCommentIdAndUserAccount_UserId(3L, 1L);

        // Then
        assertThat(adminPostCommentRepository.count()).isEqualTo(previousAdminCommentCount - 1); // 테스트 댓글 + 대댓글 4개
    }

    @DisplayName("[Querydsl] 전체 hashtag 리스트에서 이름만 조회하기")
    @Test
    void givenNothing_whenQueryingHashtags_thenReturnsHashtagNames() {
        // Given

        // When
        List<String> hashtagNames = hashtagRepository.findAllHashtagNames();

        // Then
        assertThat(hashtagNames).hasSize(19);
    }

    @DisplayName("[Querydsl] hashtag로 페이징된 게시글 검색하기")
    @Test
    void givenHashtagNamesAndPageable_whenQueryingAdminPosts_thenReturnsAdminPostPage() {
        // Given
        List<String> hashtagNames = List.of("blue", "crimson", "fuscia");
        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.desc("hashtags.hashtagName"),
                Sort.Order.asc("homeTitle")
        ));

        // When
        Page<AdminPost> adminPostPage = adminPostRepository.findByHashtagNames(hashtagNames, pageable);

        // Then
        assertThat(adminPostPage.getContent()).hasSize(pageable.getPageSize());
        assertThat(adminPostPage.getContent().get(0).getHomeDetails()).isEqualTo("Fusce posuere felis sed lacus.");
        assertThat(adminPostPage.getContent().get(0).getHashtags())
                .extracting("hashtagName", String.class)
                .containsExactly("fuscia");
        assertThat(adminPostPage.getTotalElements()).isEqualTo(17);
        assertThat(adminPostPage.getTotalPages()).isEqualTo(4);
    }


    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }

}