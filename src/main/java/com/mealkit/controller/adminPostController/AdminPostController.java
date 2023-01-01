package com.mealkit.controller.adminPostController;


import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.constant.City;
import com.mealkit.domain.constant.FormStatus;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.Dto.request.AdminPostRequest;
import com.mealkit.domain.post.admin.Dto.response.AdminPostResponse;
import com.mealkit.domain.post.admin.Dto.response.AdminPostWithCommentResponse;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.repository.UserRepository;
import com.mealkit.service.AdminPostService;
import com.mealkit.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/homes")
@RequiredArgsConstructor
@Controller
public class AdminPostController {

    @Autowired
    private UserRepository userRepsitory;

    private final AdminPostService adminPostService;
    private final PaginationService paginationService;
    @GetMapping("/posts")
    public String adminPosts(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) City city,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {

        Page<AdminPostResponse> adminPost = adminPostService.searchAdminPosts(searchType, searchValue, city, pageable).map(AdminPostResponse::from);
        log.info("cheeck : " + adminPost);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), adminPost.getTotalPages());
        System.out.println(adminPost);

        System.out.println(barNumbers);
        System.out.println(SearchType.values());

        map.addAttribute("adminPost", adminPost);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "Homes/homes";
    }

    @GetMapping("posts/{adminPostId}")
    public String adminPost(@PathVariable Long adminPostId, ModelMap map) {

        AdminPostWithCommentResponse adminPost = AdminPostWithCommentResponse.from(adminPostService.
                getAdminPostWithComments(adminPostId));

        System.out.println(adminPost);
        System.out.println("details : " + adminPost.homeVideo());
        map.addAttribute("adminPost", adminPost);
        map.addAttribute("adminPostComment", adminPost.adminPostCommentResponse());
        System.out.println(adminPost.adminPostCommentResponse());
        map.addAttribute("totalCount", adminPostService.getAdminPostCount());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);
        map.addAttribute("key",adminPost.homeVideo());

        return "Homes/details";
    }

    @GetMapping("posts/search-hashtag")
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

    @GetMapping("posts/form")
    public String adminPostForm(ModelMap map) {

        System.out.println("reload1");
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "AdminPost/upload";
    }

    @PostMapping("/formSend")
    public String postNewAdminPosts(
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
             @RequestBody AdminPostRequest adminPostRequest , ModelMap modelMap) {
        System.out.println("fromSend arrived : " + adminPostRequest + " userDetails : " + userDetailsImplement.getUserAccount().getUserId());
        UserAccountDto user = UserAccountDto.from(userRepsitory.findByUserName(userDetailsImplement.getUsername()));
        adminPostService.saveAdminPost(adminPostRequest.toDto(user));
         modelMap.addAttribute("done", "done");
        return "AdminPost/upload";
    }

    @GetMapping("/posts/{adminPostId}/form")
    public String updateAdminPostForm(@PathVariable Long adminPostId, ModelMap map) {
        AdminPostResponse adminPost = AdminPostResponse.from(adminPostService.getAdminPost(adminPostId));

        map.addAttribute("adminPost", adminPost);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return  "AdminPost/upload";
    }

    @PostMapping("/{homeId}/form")
    public String updateAdminPost(
            @PathVariable Long homeId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
            @RequestBody AdminPostRequest adminPostRequest
    ) {
        System.out.println("arrived 수정 : " + homeId );
        System.out.println(userDetailsImplement.getUserAccount().getUserId());
        adminPostService.updateAdminPost(homeId, adminPostRequest.toDto(userDetailsImplement.toDto()));

        return "redirect:/homeId/" + homeId;
    }

    @PostMapping("/{homeId}/delete")
    public String deleteAdminPost(
            @PathVariable Long homeId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement
    ) {
        System.out.println("delete Check");
        adminPostService.deleteAdminPost(homeId, userDetailsImplement.getUserAccount().getUserId());

        return "redirect:/adminPosts";
    }

}
