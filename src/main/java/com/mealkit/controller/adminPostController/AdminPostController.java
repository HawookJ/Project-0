package com.mealkit.controller.adminPostController;


import com.mealkit.domain.constant.FormStatus;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.Dto.request.AdminPostRequest;
import com.mealkit.domain.post.admin.Dto.response.AdminPostResponse;
import com.mealkit.domain.post.admin.Dto.response.AdminPostWithCommentResponse;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.service.AdminPostService;
import com.mealkit.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequestMapping("/adminPosts")
@RequiredArgsConstructor
@Controller
public class AdminPostController {


    private final AdminPostService adminPostService;
    private final PaginationService paginationService;
    @GetMapping
    public String adminPosts(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {

        Page<AdminPostResponse> adminPost = adminPostService.searchAdminPosts(searchType, searchValue, pageable).map(AdminPostResponse::from);
        log.info("cheeck : " + adminPost);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), adminPost.getTotalPages());

        map.addAttribute("adminPost", adminPost);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "AdminPost/index";
    }

    @GetMapping("/{adminPostId}")
    public String adminPost(@PathVariable Long adminPostId, ModelMap map) {
        AdminPostWithCommentResponse adminPost = AdminPostWithCommentResponse.from(adminPostService.
                getAdminPostWithComments(adminPostId));

        map.addAttribute("adminPost", adminPost);
        map.addAttribute("adminPostComment", adminPost.adminPostCommentResponse());
        map.addAttribute("totalCount", adminPostService.getAdminPostCount());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "adminPost/details";
    }

    @GetMapping("/search-hashtag")
    public String searchAdminPostHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, ModelMap map
    ) {

        Page<AdminPostResponse> adminPost = adminPostService.searchAdminPostViaHashtag(searchValue, pageable).map(AdminPostResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), adminPost.getTotalPages());
        List<String> hashtags = adminPostService.getHashtags();


        map.addAttribute("adminPost", adminPost);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "adminPost/search-hashtag";
    }

    @GetMapping("/form")
    public String adminPostForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "AdminPost/upload";
    }

    @PostMapping("/form")
    public String postNewArticle(
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
            AdminPostRequest adminPostRequest
    ) {
        adminPostService.saveAdminPost(adminPostRequest.toDto(userDetailsImplement.toDto()));

        return "redirect:/AdminPost/upload";
    }

    @GetMapping("/{adminPostId}/form")
    public String updateAdminPostForm(@PathVariable Long adminPostId, ModelMap map) {
        AdminPostResponse adminPost = AdminPostResponse.from(adminPostService.getAdminPost(adminPostId));

        map.addAttribute("adminPost", adminPost);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
            AdminPostRequest adminPostRequest
    ) {
        adminPostService.updateAdminPost(articleId, adminPostRequest.toDto(userDetailsImplement.toDto()));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement
    ) {
        adminPostService.deleteAdminPost(articleId, userDetailsImplement.getUserAccount().getUserId());

        return "redirect:/aminPosts";
    }

}
