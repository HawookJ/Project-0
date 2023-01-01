package com.mealkit.domain.post.user.Dto;

import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.user.UserPostComment;

import java.time.LocalDateTime;

public record UserPostCommentDto (
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        Long commentId,
        String commentContent,
        UserAccountDto userAccountDto,
        Long parentCommentId


        ){
public static UserPostCommentDto of(

        Long commentId,
        String commentContent,
        UserAccountDto userAccountDto

){
        System.out.println("UserPostCommentDto only+contents");
        return UserPostCommentDto.of(commentId, commentContent, userAccountDto, null);
}

public static UserPostCommentDto of(Long commentId,
        String commentContent,
        UserAccountDto userAccountDto,
        Long parentCommentId)
        {
                System.out.println("UserPostCommentDto null+contents");
      return UserPostCommentDto.of(null,null,null,null,commentId, commentContent, userAccountDto, parentCommentId);
}

public static UserPostCommentDto of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy,
                                        Long commentId,
                                        String commentContent,
                                        UserAccountDto userAccountDto,
                                        Long parentCommentId){
        return new UserPostCommentDto(createdAt, createdBy, modifiedAt, modifiedBy, commentId, commentContent, userAccountDto, parentCommentId);
}




        //모아서 PostWithComment 쪽으로 한번에 보내준다
public static UserPostCommentDto from(UserPostComment entity){
return new UserPostCommentDto(
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy(),
        entity.getCommentId(),
        entity.getCommentContent(),
        UserAccountDto.from(entity.getUserAccount()),
        entity.getParentCommentId()
);
}




}
