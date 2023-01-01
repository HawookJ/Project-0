package com.mealkit.controller.adminPostController;


import com.mealkit.domain.post.admin.Dto.request.AdminPostCommentRequest;
import com.mealkit.jwt.domainTO.UserDetailsImplement;
import com.mealkit.service.AdminPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class AdminPostCommentController {

    private final AdminPostCommentService adminPostCommentService;

    @PostMapping("/new")
    public String postNewAdminPostComment
            ( @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
                @RequestBody AdminPostCommentRequest adminPostCommentRequest)
        {
            System.out.println(adminPostCommentRequest);
            System.out.println((userDetailsImplement.toDto()));

            System.out.println(adminPostCommentRequest.toDto(userDetailsImplement.toDto()));

        adminPostCommentService.saveAdminPostComment(adminPostCommentRequest.toDto(userDetailsImplement.toDto()));
            System.out.println("new Comment Done");
        return "redirect:/homes/posts/" + adminPostCommentRequest.adminPostId();
    }
    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImplement userDetailsImplement,
            Long homeId
    ) {
        System.out.println("삭제");
        adminPostCommentService.deleteAdminPostComment(commentId, userDetailsImplement.getUserAccount().getUserId());

        return "redirect:/homes/posts/" + homeId;
    }

}
