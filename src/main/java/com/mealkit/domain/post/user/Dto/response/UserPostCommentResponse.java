package com.mealkit.domain.post.user.Dto.response;

import com.mealkit.domain.post.user.Dto.UserPostCommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record UserPostCommentResponse(
        Long commentId,
        String userCommentContent,
        LocalDateTime createdAt,
        String userEmail,
        String nickName,
        Long parentCommentId,
        Set<UserPostCommentResponse> childComments
) {
    public static UserPostCommentResponse of(Long commentId,
                                             String userCommentContent,
                                             LocalDateTime createdAt,
                                             String userEmail,
                                             String nickName
                                             ) {
        return UserPostCommentResponse.of(commentId, userCommentContent,
                createdAt, userEmail, nickName, null);
    }

    public static UserPostCommentResponse of(Long commentId,
                                             String userCommentContent,
                                             LocalDateTime createdAt,
                                               String userEmail,
                                             String nickName,
                                             Long parentCommentId) {
        Comparator<UserPostCommentResponse> childCommentComparator = Comparator
                .comparing(UserPostCommentResponse::createdAt)
                .thenComparing(UserPostCommentResponse::commentId);

        return new UserPostCommentResponse(commentId, userCommentContent, createdAt, userEmail, nickName, parentCommentId, new TreeSet<>(childCommentComparator));
    }

    public static UserPostCommentResponse from(UserPostCommentDto dto) {
        String nickName = dto.userAccountDto().nickName();
        if (nickName == null || nickName.isBlank()) {
            nickName = dto.userAccountDto().userEmail();
        }
        return UserPostCommentResponse.of(
                dto.commentId(),
                dto.commentContent(),
                dto.createdAt(),
                dto.userAccountDto().userEmail(),
                nickName,
                dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId!=null;
    }
}
