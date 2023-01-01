package com.mealkit.controller.userPostController;

import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.constant.FormStatus;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.Dto.request.AdminPostRequest;
import com.mealkit.domain.post.user.Dto.request.UserPostRequest;
import com.mealkit.domain.post.user.Dto.response.UserPostResponse;
import com.mealkit.domain.post.user.Dto.response.UserPostWithCommentResponse;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.repository.UserRepository;
import com.mealkit.service.PaginationService;
import com.mealkit.service.UserPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/community")
@RequiredArgsConstructor
@Controller
public class userPostController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPostService userPostService;

    @Autowired
    private PaginationService paginationService;

    @GetMapping
    public String userPosts(
            @RequestParam(required= false) String searchValue,
            @RequestParam(required = false) SearchType searchType,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            ModelMap modelMap
    ){

     Page<UserPostResponse> userPost = userPostService.searchUserPosts(searchValue, searchType, pageable).map(UserPostResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), userPost.getTotalPages());
        modelMap.addAttribute("userPost", userPost);
        modelMap.addAttribute("paginationBarNumbers", barNumbers);
        modelMap.addAttribute("searchTypes", SearchType.values());




        return "Community/community";
    }



    @GetMapping("/{userPostId}")
    public String userPost(@PathVariable Long userPostId, ModelMap modelMap){

        UserPostWithCommentResponse userPost= UserPostWithCommentResponse.from(userPostService.getUserPostWithComments(userPostId));
        modelMap.addAttribute("userPost", userPost);
        modelMap.addAttribute("userPostComment", userPost.userPostCommentDtos());
        modelMap.addAttribute("totalCount", userPostService.getUserPostCount());
        System.out.println(userPost);

        return "Community/details";
    }

    @PostMapping("/formSend")
    public String PostNewUserPost(
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
            @RequestBody UserPostRequest userPostRequest
    ){
        System.out.println("userPost arrived : " + userPostRequest );
        UserAccountDto user= UserAccountDto.from(userRepository.findByUserName(userDetailsImplement.getUsername()));
        userPostService.saveUserPost(userPostRequest.toDto(user));
        return "redirect:/Community/community";
    }

    @GetMapping("/form")
    public String userPostForm(ModelMap map) {

        System.out.println("reload1");
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "Community/upload";
    }

    @GetMapping("/{userPostId}/form")
    public String updateUserPostForm(@PathVariable Long userPostId, ModelMap model){
        UserPostResponse userPost= UserPostResponse.from(userPostService.getUserPost(userPostId));
   model.addAttribute("userPost",userPost);
   model.addAttribute("formStatus", FormStatus.UPDATE);
   return "Community/upload";

    }

    @PostMapping("/{userPostId}/form")
    public String updateUserPost(
            @PathVariable Long userPostId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
           UserPostRequest userPostRequest
    ) {
        userPostService.updateUserPost(userPostId, userPostRequest.toDto(userDetailsImplement.toDto()));

        return "redirect:/userPostId/" + userPostId;
    }



    @PostMapping("/{userPostId}/delete")
    public String deleteUserPost(
            @PathVariable Long userPostId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement
    ) {
        System.out.println("delete Check");
        userPostService.deleteUserPost(userPostId, userDetailsImplement.getUserAccount().getUserId());

        return "redirect:/userPosts";
    }


}
