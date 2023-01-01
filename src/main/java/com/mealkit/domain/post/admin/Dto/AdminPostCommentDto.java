package com.mealkit.domain.post.admin.Dto;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.AdminPostComment;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.mealkit.domain.post.admin.AdminPostComment} entity
 */
public record AdminPostCommentDto(LocalDateTime createdAt,
                                  String createdBy,
                                  LocalDateTime modifiedAt,
                                  String modifiedBy,
                                  Long adminPostCommentId,
                                  UserAccountDto userAccountDto,
                                  String adminCommentContent,
                                  Long adminPostId,
                                  Long parentCommentId) {

    //for test
    public static AdminPostCommentDto of(Long adminPostId, UserAccountDto userAccountDto, String adminCommentContent) {
        System.out.println("test");
        return AdminPostCommentDto.of(adminPostId, userAccountDto, adminCommentContent, null);
    }

    public static AdminPostCommentDto of(Long adminPostId, UserAccountDto userAccountDto, String adminCommentContent, Long parentCommentId) {
        System.out.println("working");
        return AdminPostCommentDto.of(null, null, null, null, null, userAccountDto, adminCommentContent, adminPostId, parentCommentId);
    }


    public static AdminPostCommentDto of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
                                         String modifiedBy, Long adminPostCommentId, UserAccountDto userAccountDto,
                                         String adminCommentContent, Long adminPostId, Long parentCommentId) {

        return new AdminPostCommentDto(createdAt, createdBy, modifiedAt, modifiedBy, adminPostCommentId, userAccountDto,
                adminCommentContent, adminPostId, parentCommentId);
    }


    public static AdminPostCommentDto from(AdminPostComment entity) {
        return new AdminPostCommentDto(
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getAdminPostCommentId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getAdminCommentContent(),
                entity.getAdminPost().getHomeId(),
                entity.getParentCommentId());

    }


    public AdminPostComment toEntity(AdminPost adminPost, UserAccount userAccount) {
        return AdminPostComment.of(userAccount, adminCommentContent, adminPost);
    }

}