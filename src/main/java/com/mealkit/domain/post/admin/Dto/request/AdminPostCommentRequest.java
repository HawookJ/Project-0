package com.mealkit.domain.post.admin.Dto.request;

import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;

public record AdminPostCommentRequest (
    Long adminPostId,
    Long parentCommentId,
    String adminCommentContent
) {

        public static AdminPostCommentRequest of(Long adminPostId, String content) {
            return AdminPostCommentRequest.of(adminPostId, null, content);
        }

        public static AdminPostCommentRequest of(Long adminPostId, Long parentCommentId, String content) {
            return new AdminPostCommentRequest(adminPostId, parentCommentId, content);
        }

        public AdminPostCommentDto toDto(UserAccountDto userAccountDto) {
            return AdminPostCommentDto.of(
                  adminPostId, userAccountDto, adminCommentContent,parentCommentId
            );
        }

    }