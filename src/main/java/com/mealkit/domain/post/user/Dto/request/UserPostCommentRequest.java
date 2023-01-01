package com.mealkit.domain.post.user.Dto.request;

import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.user.Dto.UserPostCommentDto;

public record UserPostCommentRequest (
        Long userPostId,
        Long parentCommentId,
        String userCommentContent
){
    public static UserPostCommentRequest of(Long userPostId, String userCommentContent){
        return UserPostCommentRequest.of(userPostId, null, userCommentContent);
    }

    public static UserPostCommentRequest of(Long userPostId, Long parentCommentId, String userCommentContent)
    {
        return new UserPostCommentRequest(userPostId, parentCommentId, userCommentContent);
    }

    public UserPostCommentDto toDto(UserAccountDto userAccountDto){
        return UserPostCommentDto.of(userPostId, userCommentContent, userAccountDto,  parentCommentId);
    }

}
