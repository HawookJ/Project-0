package com.mealkit.service;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;

import com.mealkit.domain.post.admin.Dto.AdminPostWithCommentDto;
import com.mealkit.repository.AdminPostRepository;
import com.mealkit.repository.HashtagRepository;
import com.mealkit.repository.UserRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.as;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@DisplayName("게시글 리스트 뽑는 비지니스 로직")
@ExtendWith(MockitoExtension.class) //테스트는 가볍게 스프링부트 지원 받지 않고..DI 필요할시 mockito 사용
class AdminPostServiceTest {

    @InjectMocks private AdminPostService adminPostService;

    @Mock private HashtagService hashtagService;
    @Mock private AdminPostRepository adminPostRepository;
    @Mock private UserRepository userRepository;
    @Mock private HashtagRepository hashtagRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingAdminPosts_thenReturnsAdminPostPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(adminPostRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<AdminPostDto> adminPost = adminPostService.searchAdminPosts(null, null,null, pageable);

        // Then
        assertThat(adminPost).isEmpty();
        then(adminPostRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingAdminPosts_thenReturnsAdminPostPage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(adminPostRepository.findByHomeTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<AdminPostDto> adminPost = adminPostService.searchAdminPosts(searchType, searchKeyword,null, pageable);

        // Then
        assertThat(adminPost).isEmpty();
        then(adminPostRepository).should().findByHomeTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingAdminPostsViaHashtag_thenReturnsEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<AdminPostDto> adminPost = adminPostService.searchAdminPostViaHashtag(null, pageable);

        // Then
        assertThat(adminPost).isEqualTo(Page.empty(pageable));
        then(hashtagRepository).shouldHaveNoInteractions();
        then(adminPostRepository).shouldHaveNoInteractions();
    }

    @DisplayName("없는 해시태그를 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNonexistentHashtag_whenSearchingAdminPostsViaHashtag_thenReturnsEmptyPage() {
        // Given
        String hashtagName = "난 없지롱";
        Pageable pageable = Pageable.ofSize(20);
        given(adminPostRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(), pageable, 0));

        // When
        Page<AdminPostDto> adminPost = adminPostService.searchAdminPostViaHashtag(hashtagName, pageable);

        // Then
        assertThat(adminPost).isEqualTo(Page.empty(pageable));
        then(adminPostRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
    }
    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingAdminPostsViaHashtag_thenReturnsAdminPostsPage() {
        // Given
        String hashtagName = "java";
        Pageable pageable = Pageable.ofSize(20);
        AdminPost expectedAdminPost = createAdminPost();
        given(adminPostRepository.findByHashtagNames(List.of(hashtagName), pageable)).willReturn(new PageImpl<>(List.of(expectedAdminPost), pageable, 1));

        // When
        Page<AdminPostDto> adminPosts = adminPostService.searchAdminPostViaHashtag(hashtagName, pageable);

        // Then
        assertThat(adminPosts).isEqualTo(new PageImpl<>(List.of(AdminPostDto.from(expectedAdminPost)), pageable, 1));
        then(adminPostRepository).should().findByHashtagNames(List.of(hashtagName), pageable);
    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenAdminPostId_whenSearchingAdminPostWithComments_thenReturnsAdminPostWithComments() {
        // Given
        Long adminPostId = 3L;
        AdminPost adminPost = createAdminPost();
        given(adminPostRepository.findById(adminPostId)).willReturn(Optional.of(adminPost));

        // When
        AdminPostWithCommentDto dto = adminPostService.getAdminPostWithComments(adminPostId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("homeTitle", adminPost.getHomeTitle())
                .hasFieldOrPropertyWithValue("homeDetails", adminPost.getHomeDetails())
                .hasFieldOrPropertyWithValue("hashtagDtos", adminPost.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet())
                );
        then(adminPostRepository).should().findById(adminPostId);
    }



    @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentAdminPostId_whenSearchingAdminPostWithComments_thenThrowsException() {
        // Given
        Long adminPostId = 60L;
        given(adminPostRepository.findById(adminPostId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> adminPostService.getAdminPostWithComments(adminPostId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - admionPostId: " + adminPostId);
        then(adminPostRepository).should().findById(adminPostId);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenAdminPostId_whenSearchingAdminPost_thenReturnsAdminPost() {
        // Given
        Long adminPostId = 1L;
        AdminPost adminPost = createAdminPost();
        given(adminPostRepository.findById(adminPostId)).willReturn(Optional.of(adminPost));

        // When
        AdminPostDto dto = adminPostService.getAdminPost(adminPostId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("homeTitle", adminPost.getHomeTitle())
                .hasFieldOrPropertyWithValue("homeDetails", adminPost.getHomeDetails())
                .hasFieldOrPropertyWithValue("hashtagDtos", adminPost.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet())
                );
        then(adminPostRepository).should().findById(adminPostId);
    }

    @DisplayName("게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentAdminPostId_whenSearchingAdminPost_thenThrowsException() {
        // Given
        Long adminPostId = 0L;
        given(adminPostRepository.findById(adminPostId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> adminPostService.getAdminPost(adminPostId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - adminPostId: " + adminPostId);
        then(adminPostRepository).should().findById(adminPostId);
    }

    @DisplayName("게시글 정보를 입력하면, 본문에서 해시태그 정보를 추출하여 해시태그 정보가 포함된 게시글을 생성한다.")
    @Test
    void givenAdminPostInfo_whenSavingAdminPost_thenExtractsHashtagsFromContentAndSavesAdminPostWithExtractedHashtags() {
        // Given
        AdminPostDto dto = createAdminPostDto();
        Set<String> expectedHashtagNames = Set.of("java", "spring");
        Set<Hashtag> expectedHashtags = new HashSet<>();
        expectedHashtags.add(createHashtag("java"));

        given(userRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(hashtagService.parseHashtagNames(dto.homeDetails())).willReturn(expectedHashtagNames);
        given(hashtagService.findHashtagsByNames(expectedHashtagNames)).willReturn(expectedHashtags);
        given(adminPostRepository.save(any(AdminPost.class))).willReturn(createAdminPost());

        // When
        adminPostService.saveAdminPost(dto);

        // Then
        then(userRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(hashtagService).should().parseHashtagNames(dto.homeDetails());
        then(hashtagService).should().findHashtagsByNames(expectedHashtagNames);
        then(adminPostRepository).should().save(any(AdminPost.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedAdminPostInfo_whenUpdatingAdminPost_thenUpdatesAdminPost() {
        // Given
        AdminPost adminPost = createAdminPost();
        AdminPostDto dto = createAdminPostDto(3L,"새 타이틀", "새 내용 #springboot");
        Set<String> expectedHashtagNames = Set.of("springboot");
        Set<Hashtag> expectedHashtags = new HashSet<>();

        given(adminPostRepository.getReferenceById(dto.homeId())).willReturn(adminPost);
        given(userRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());
        willDoNothing().given(adminPostRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutAdminPost(any());
        given(hashtagService.parseHashtagNames(dto.homeDetails())).willReturn(expectedHashtagNames);
        given(hashtagService.findHashtagsByNames(expectedHashtagNames)).willReturn(expectedHashtags);

        // When
        adminPostService.updateAdminPost(dto.homeId(), dto);

        // Then
        assertThat(adminPost)
                .hasFieldOrPropertyWithValue("homeTitle", dto.homeTitle())
                .hasFieldOrPropertyWithValue("homeDetails", dto.homeDetails())
                .extracting("hashtags", as(InstanceOfAssertFactories.COLLECTION))
                .hasSize(1)
                .extracting("hashtagName")
                .containsExactly("springboot");
        then(adminPostRepository).should().getReferenceById(dto.homeId());
        then(userRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(adminPostRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutAdminPost(any());
        then(hashtagService).should().parseHashtagNames(dto.homeDetails());
        then(hashtagService).should().findHashtagsByNames(expectedHashtagNames);

    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentAdminPostInfo_whenUpdatingAdminPost_thenLogsWarningAndDoesNothing() {
        // Given
        AdminPostDto dto = createAdminPostDto(4L,"새 타이틀", "새 내용");
        given(adminPostRepository.getReferenceById(dto.homeId())).willThrow(EntityNotFoundException.class);

        // When
        adminPostService.updateAdminPost(dto.homeId(), dto);

        // Then
        then(adminPostRepository).should().getReferenceById(dto.homeId());
        then(userRepository).shouldHaveNoInteractions();
        then(hashtagService).shouldHaveNoInteractions();
    }

    @DisplayName("게시글 작성자가 아닌 사람이 수정 정보를 입력하면, 아무 것도 하지 않는다.")
    @Test
    void givenModifiedAdminPostInfoWithDifferentUser_whenUpdatingAdminPost_thenDoesNothing() {
        // Given
        Long differentAdminPostId = 22L;
        AdminPost differentAdminPost = createAdminPost(differentAdminPostId);
        differentAdminPost.setUserAccount(createUserAccount(3L));
        AdminPostDto dto = createAdminPostDto(3L,"새 타이틀", "새 내용");
        given(adminPostRepository.getReferenceById(differentAdminPostId)).willReturn(differentAdminPost);
        given(userRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());

        // When
        adminPostService.updateAdminPost(differentAdminPostId, dto);

        // Then
        then(adminPostRepository).should().getReferenceById(differentAdminPostId);
        then(userRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(hashtagService).shouldHaveNoInteractions();
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenAdminPostId_whenDeletingAdminPost_thenDeletesAdminPost() {
        // Given
        Long homeId = 1L;
        Long userId = 3L;
        given(adminPostRepository.getReferenceById(homeId)).willReturn(createAdminPost());
        willDoNothing().given(adminPostRepository).deleteByHomeIdAndUserAccount_UserId(homeId, userId);
        willDoNothing().given(adminPostRepository).flush();
        willDoNothing().given(hashtagService).deleteHashtagWithoutAdminPost(any());

        // When
        adminPostService.deleteAdminPost(1L, userId);

        // Then
        then(adminPostRepository).should().getReferenceById(homeId);
        then(adminPostRepository).should().deleteByHomeIdAndUserAccount_UserId(homeId, userId);
        then(adminPostRepository).should().flush();
        then(hashtagService).should(times(2)).deleteHashtagWithoutAdminPost(any());
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingAdminPosts_thenReturnsAdminPostCount() {
        // Given
        long expected = 0L;
        given(adminPostRepository.count()).willReturn(expected);

        // When
        long actual = adminPostService.getAdminPostCount();

        // Then
        assertThat(actual).isEqualTo(expected);
        then(adminPostRepository).should().count();
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        AdminPost adminPost = createAdminPost();
        List<String> expectedHashtags = List.of("java", "spring", "boot");
        given(hashtagRepository.findAllHashtagNames()).willReturn(expectedHashtags);

        // When
        List<String> actualHashtags = adminPostService.getHashtags();

        // Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(hashtagRepository).should().findAllHashtagNames();
    }

    private UserAccount createUserAccount() {
        return createUserAccount(3L);
    }

    private UserAccount createUserAccount(Long userId) {
        return UserAccount.of(
                userId,
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
        return createAdminPost(52L);
    }

    private AdminPost createAdminPost(Long homeId) {
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
        adminPost.addHashtags(Set.of(
                createHashtag(1L, "java"),
                createHashtag(2L, "spring")
        ));
        ReflectionTestUtils.setField(adminPost, "homeId", homeId);

        return adminPost;
    }

    private Hashtag createHashtag(String hashtagName) {
        return createHashtag(1L, hashtagName);
    }

    private Hashtag createHashtag(Long id, String hashtagName) {
        Hashtag hashtag = Hashtag.of(hashtagName);
        ReflectionTestUtils.setField(hashtag, "id", id);

        return hashtag;
    }

    private HashtagDto createHashtagDto() {
        return HashtagDto.of("java");
    }

    private AdminPostDto createAdminPostDto() {
        return createAdminPostDto(2L, "title", "details");
    }

    private AdminPostDto createAdminPostDto(Long homeId, String homeTitle, String homeDetails) {
        return AdminPostDto.of(
                homeId,
                createUserAccountDto(),
                Set.of(HashtagDto.of("java")),
                "home",
                "addrss",
                "homenumber",
                true,
                32,
                34,
                "homeRegi",
                "youtube",
                "lucnch",
                new Board(5l,5l ),
                43L,
                "content",
                "title" ,
                null,
                null,
                null,
                null);
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                3L,
                "username",
                "nickname",
                "uno@mail.com",
                "Uno-child",
                1,
                "userMemo",
                "uno=password"

        );
    }


}