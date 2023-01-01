package com.mealkit.service;


import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.AdminPostComment;
import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;

import com.mealkit.repository.AdminPostCommentRepository;
import com.mealkit.repository.AdminPostRepository;
import com.mealkit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@DisplayName("댓글 서비스")
@ExtendWith(MockitoExtension.class)
class AdminPostCommentServiceTest {

    @InjectMocks
    private AdminPostCommentService adminPostCommentService;

    @Mock
    private AdminPostCommentRepository adminPostCommentRepository;
    @Mock
    private AdminPostRepository adminPostRepository;


    @Mock private UserRepository userRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long homeId = 3L;
        AdminPostComment expectedParentComment = createAdminPostComment(1L, "parent content");
        AdminPostComment expectedChildComment = createAdminPostComment(2L, "child content");
        expectedChildComment.setParentCommentId(expectedParentComment.getParentCommentId());
        given(adminPostCommentRepository.findByAdminPost_HomeId(homeId)).willReturn(List.of(
                expectedParentComment,
                expectedChildComment
        ));

        // When
        List<AdminPostCommentDto> actual = adminPostCommentService.searchAdminPostComment(homeId);

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual)
                .extracting("userId", "homeId", "parentCommentId", "homeDetails")
                .containsExactlyInAnyOrder(
                        tuple(1L, 1L, null, "parent content"),
                        tuple(2L, 1L, 1L, "child content")
                );
        then(adminPostCommentRepository).should().findByAdminPost_HomeId(homeId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        AdminPostCommentDto dto = createAdminPostCommentDto("댓글");
        given(adminPostRepository.getReferenceById(dto.adminPostId())).willReturn(createAdminPost());
        given(userRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(adminPostCommentRepository.save(any(AdminPostComment.class))).willReturn(null);

        // When
        adminPostCommentService.saveAdminPostComment(dto);

        // Then
        then(adminPostRepository).should().getReferenceById(dto.adminPostId());
        then(userRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(adminPostCommentRepository).should(never()).getReferenceById(anyLong());
        then(adminPostCommentRepository).should().save(any(AdminPostComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        AdminPostCommentDto dto = createAdminPostCommentDto("댓글");
        given(adminPostRepository.getReferenceById(dto.adminPostId())).willThrow(EntityNotFoundException.class);

        // When
        adminPostCommentService.saveAdminPostComment(dto);

        // Then
        then(adminPostRepository).should().getReferenceById(dto.adminPostId());
        then(userRepository).shouldHaveNoInteractions();
        then(adminPostCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("부모 댓글 ID와 댓글 정보를 입력하면, 대댓글을 저장한다.")
    @Test
    void givenParentCommentIdAndArticleCommentInfo_whenSaving_thenSavesChildComment() {
        // Given
        Long parentCommentId = 1L;
        AdminPostComment parent = createAdminPostComment(parentCommentId, "댓글");
        System.out.println("check");
        AdminPostCommentDto child = createAdminPostCommentDto(parentCommentId, "대댓글");
        given(adminPostRepository.getReferenceById(child.adminPostId())).willReturn(createAdminPost());
        given(userRepository.getReferenceById(child.userAccountDto().userId())).willReturn(createUserAccount());
        given(adminPostCommentRepository.getReferenceById(child.parentCommentId())).willReturn(parent);

        // When
        adminPostCommentService.saveAdminPostComment(child);

        // Then
        assertThat(child.parentCommentId()).isNotNull();
        then(adminPostRepository).should().getReferenceById(child.adminPostId());
        then(userRepository).should().getReferenceById(child.userAccountDto().userId());
        then(adminPostCommentRepository).should().getReferenceById(child.parentCommentId());
        then(adminPostCommentRepository).should(never()).save(any(AdminPostComment.class));
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long adminPostCommentId = 1L;
        Long userId = 1L;
        willDoNothing().given(adminPostCommentRepository).deleteByAdminPostCommentIdAndUserAccount_UserId(adminPostCommentId, userId);

        // When
        adminPostCommentService.deleteAdminPostComment(adminPostCommentId, userId);

        // Then
        then(adminPostCommentRepository).should().deleteByAdminPostCommentIdAndUserAccount_UserId(adminPostCommentId, userId);
    }


    private AdminPostCommentDto createAdminPostCommentDto(String content) {
        return createAdminPostCommentDto(null, content);
    }

    private AdminPostCommentDto createAdminPostCommentDto(Long parentCommentId, String content) {
        return createAdminPostCommentDto(1L, parentCommentId, content);
    }

    private AdminPostCommentDto createAdminPostCommentDto(Long id, Long parentCommentId, String content) {
        return AdminPostCommentDto.of(
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno",
                id,
                createUserAccountDto(),
                content,
                1L,
               parentCommentId


        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                2L,
                "username",
                "nickname",
                "uno@mail.com",
                "Uno-child",
                1,
                "userMemo",
                "uno=password"

        );
    }

    private AdminPostComment createAdminPostComment(Long id, String content) {
        AdminPostComment adminPostComment = AdminPostComment.of(
                createUserAccount(),
                content,
                createAdminPost()


        );
        ReflectionTestUtils.setField(adminPostComment, "adminPostCommentId", id);

        return adminPostComment;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                2L,
                "userName2",
                1,
                "Uno#@Elekr",
                "sonny",
                "passwordddyh",
                "ninick,",

                "mememo> ",
                "?navver",
                RoleType.USER

        );
    }

    private AdminPost createAdminPost() {
        AdminPost adminPost = AdminPost.of(
                createUserAccount(),
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
                "titleHOme"

        );
        ReflectionTestUtils.setField(adminPost, "homeId", 2L);
        adminPost.addHashtags(Set.of(createHashtag(adminPost)));

        return adminPost;
    }

    private Hashtag createHashtag(AdminPost adminPost) {
        return Hashtag.of("java");
    }
}