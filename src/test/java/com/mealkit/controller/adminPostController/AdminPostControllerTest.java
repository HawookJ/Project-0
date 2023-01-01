package com.mealkit.controller.adminPostController;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.constant.City;
import com.mealkit.domain.constant.FormStatus;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;
import com.mealkit.domain.post.admin.Dto.AdminPostWithCommentDto;
import com.mealkit.domain.post.admin.Dto.request.AdminPostRequest;
import com.mealkit.domain.post.admin.Dto.response.AdminPostResponse;
import com.mealkit.service.AdminPostService;
import com.mealkit.service.PaginationService;
import com.mealkit.util.FormDataEncoder;
import com.mealkit.util.TestSecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("view 게시글 컨트롤러")
@WebMvcTest(AdminPostController.class)
@Import({TestSecurityConfig.class, FormDataEncoder.class})
class AdminPostControllerTest {

    @MockBean
    private AdminPostService adminPostService;
    @MockBean
    private PaginationService paginationService;

    private MockMvc mvc;
    @MockBean
    private FormDataEncoder formDataEncoder;
    public AdminPostControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("[view][list] 게시글 리스트(게시판) 페이지 =호출")
    public void givenNothing_whenRequestAdminPost_thenReturnAdminPost() throws Exception {
        // Given
        given(adminPostService.searchAdminPosts(eq(null), eq(null),eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When
        mvc.perform(get("/adminPost"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/index")) //여기서 보여준다 = 여기에 뷰가 있어야한다!
                .andExpect(model().attributeExists("adminPost")) //view로 보냈을때 adminPosts라는 매핑 여부확인;
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"))
                .andExpect(model().attribute("searchTypeHashtag", SearchType.HASHTAG));


        then(adminPostService).should().searchAdminPosts(eq(null), eq(null),eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    // Then

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        City city = City.BUSAN;
        String searchValue = "homeTitle";
        given(adminPostService.searchAdminPosts(eq(searchType), eq(searchValue),eq(city), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(
                        get("/adminPosts")
                                .queryParam("searchType", searchType.name())
                                .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPosts/index"))
                .andExpect(model().attributeExists("adminPost"))
                .andExpect(model().attributeExists("searchTypes"));
        then(adminPostService).should().searchAdminPosts(eq(searchType), eq(searchValue),eq(city), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 -페이징 , 정렬기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingAdminPost_thenReturnsAdminPosts() throws Exception {
// Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);

        given(adminPostService.searchAdminPosts(null, null,null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When
        mvc.perform(
                        get("/adminPosts")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)

                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPosts/index"))
                .andExpect(model().attributeExists("adminPost"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
// Then
        then(adminPostService).should().searchAdminPosts(null, null,null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());

    }

    @DisplayName("[view][GET] 게시글 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestAdminPost_thenRedirectToLoginPage() throws Exception {
        // Given
        long adminPostId = 1L;


        // When

        mvc.perform(
                        get("/adminPosts/" + adminPostId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));


        // Then

        then(adminPostService).shouldHaveNoInteractions();
        then(adminPostService).shouldHaveNoInteractions();

    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenAuthorization_whenRequestAdminPost_thenReturnAdminPosts() throws Exception {
        // Given
        Long adminPostId = 1L;
        long totalCount = 1L;
        given(adminPostService.getAdminPostWithComments(adminPostId)).willReturn(createAdminPostWithCommentsDto());
        given(adminPostService.getAdminPostCount()).willReturn(totalCount);
        // When
        mvc.perform(get("/adminPosts" + adminPostId))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/detail"))
                .andExpect(model().attributeExists("adminPost"))
                .andExpect(model().attributeExists("adminPostComment"))
                .andExpect(model().attribute("totalCount", totalCount))
                .andExpect(model().attribute("searchTypeHashtag", SearchType.HASHTAG));
        // Then
        then(adminPostService).should().getAdminPostWithComments(adminPostId);
        then(adminPostService).should().getAdminPostCount();

    }


    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingadminPostSearchView_thenReturnsadminPostSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/adminPost/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/search"));
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingAdminPostSearchHashtagView_thenReturnsAdminPostSearchHashtagView() throws Exception {
        // Given
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(adminPostService.searchAdminPostViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(adminPostService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        // When & Then
        mvc.perform(get("/adminPost/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/search-hashtag"))
                .andExpect(model().attribute("adminPost", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(adminPostService).should().searchAdminPostViaHashtag(eq(null), any(Pageable.class));
        then(adminPostService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    void givenHashtag_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(adminPostService.searchAdminPostViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        given(adminPostService.getHashtags()).willReturn(hashtags);
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        // When & Then
        mvc.perform(
                        get("/adminPost/search-hashtag")
                                .queryParam("searchValue", hashtag)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/search-hashtag"))
                .andExpect(model().attribute("adminPost", Page.empty()))
                .andExpect(model().attribute("hashtags", hashtags))
                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
        then(adminPostService).should().searchAdminPostViaHashtag(eq(hashtag), any(Pageable.class));
        then(adminPostService).should().getHashtags();
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewAdminPostPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/adminPost/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewadminPostInfo_whenRequesting_thenSavesNewadminPost() throws Exception {
        // Given
        AdminPostRequest adminPostRequest = AdminPostRequest.of("new title", "new content", null, true, 1.3,
                3, null, null, null, null, null, "new content", "new title");
        willDoNothing().given(adminPostService).saveAdminPost(any(AdminPostDto.class));

        // When & Then
        mvc.perform(
                        post("/adminPost/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(adminPostRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPost"))
                .andExpect(redirectedUrl("/adminPost"));
        then(adminPostService).should().saveAdminPost(any(AdminPostDto.class));
    }

    @DisplayName("[view][GET] 게시글 수정 페이지 - 인증 없을 땐 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequesting_thenRedirectsToLoginPage() throws Exception {
        // Given
        long articleId = 1L;

        // When & Then
        mvc.perform(get("/adminPost/" + articleId + "/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(adminPostService).shouldHaveNoInteractions();
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenAuthorizedUser_whenRequesting_thenReturnsUpdatedArticlePage() throws Exception {
        // Given
        long articleId = 1L;
        AdminPostDto dto = createAdminPostDto();
        given(adminPostService.getAdminPost(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/adminPost/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("adminPost/form"))
                .andExpect(model().attribute("adminPost", AdminPostResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(adminPostService).should().getAdminPost(articleId);
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        AdminPostRequest articleRequest = AdminPostRequest.of("new title", "new content", null, true, 1.3,
                3, null, null, null, null, null, "new content", "new title");
        willDoNothing().given(adminPostService).updateAdminPost(eq(articleId), any(AdminPostDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(articleRequest))
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(adminPostService).should().updateAdminPost(eq(articleId), any(AdminPostDto.class));
    }

    @WithUserDetails(value = "unoTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long adminPostId = 1L;
        Long userId = 4L;
        willDoNothing().given(adminPostService).deleteAdminPost(adminPostId, userId);

        // When & Then
        mvc.perform(
                        post("/adminPost/" + adminPostId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/adminPost"))
                .andExpect(redirectedUrl("/adminPost"));
        then(adminPostService).should().deleteAdminPost(adminPostId, userId);
    }


    private AdminPostDto createAdminPostDto() {
        return AdminPostDto.of(
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
                "title"   );
    }

    private AdminPostWithCommentDto createAdminPostWithCommentsDto() {
        return AdminPostWithCommentDto.of(
                LocalDateTime.now(),
                "hawhook",
                LocalDateTime.now(),
                "hawook",
                createUserAccountDto(),
                3L,
                "homename",
                "uno",
                "3",
                true,
                345,
                4,
                "hereitis",
                "youtuvbe",
                "breakfast",
                 new Board(3L, 3L),
                23L,
                "hashtagDto",
                "hometitle",
                Set.of(HashtagDto.of("java")),
                Set.of(AdminPostCommentDto.of(3L,createUserAccountDto(),"comment"))
        );
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



