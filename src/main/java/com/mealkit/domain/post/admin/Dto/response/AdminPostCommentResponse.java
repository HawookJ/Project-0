package com.mealkit.domain.post.admin.Dto.response;

import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record AdminPostCommentResponse(
        Long adminPostCommentId,
        String adminCommentContent,
        LocalDateTime createdAt,
        String userEmail,
        String nickName,

        Long parentCommentId,
        Set<AdminPostCommentResponse> childComments
) {

    public static AdminPostCommentResponse of(Long AdminCommentId,
                                              String adminCommentContent,
                                              LocalDateTime createdAt,
                                              String userEmail,
                                              String nickName) {
        return AdminPostCommentResponse.of(AdminCommentId,
                adminCommentContent, createdAt, userEmail, nickName, null);
    }

    public static AdminPostCommentResponse of(Long adminPostCommentId,
                                              String adminCommentContent,
                                              LocalDateTime createdAt,
                                              String userEmail,
                                              String nickName,
                                              Long parentCommentId) {
        Comparator<AdminPostCommentResponse> childCommentComparator = Comparator
                .comparing(AdminPostCommentResponse::createdAt)
                .thenComparingLong(AdminPostCommentResponse::adminPostCommentId);
        return new AdminPostCommentResponse(adminPostCommentId, adminCommentContent, createdAt, userEmail, nickName, parentCommentId, new TreeSet<>(childCommentComparator));
    }

    public static AdminPostCommentResponse from(AdminPostCommentDto dto) {
        String nickName = dto.userAccountDto().nickName();
        if (nickName == null || nickName.isBlank()) {
            nickName = dto.userAccountDto().userEmail();
        }

        return AdminPostCommentResponse.of(
                dto.adminPostCommentId(),
                dto.adminCommentContent(),
                dto.createdAt(),
                dto.userAccountDto().userEmail(),
                nickName,
                dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

}