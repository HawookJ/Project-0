package com.mealkit.domain.post.user.Dto;

import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.user.PostContent;
import com.mealkit.domain.post.user.UserPost;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record UserPostWithCommentDto (
        LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
        String modifiedBy, UserAccountDto userAccountDto, Long userPostId, String title,
        PostContent postContent, Integer hidePost, Integer postLevel, Long postView,
        Set<UserPostCommentDto> userPostCommentDtos
){
    public static UserPostWithCommentDto of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
    String modifiedBy, UserAccountDto userAccountDto, Long userPostId, String title,
    PostContent postContent, Integer hidePost, Integer postLevel, Long postView,
    Set<UserPostCommentDto> userPostCommentDtos){
    return new UserPostWithCommentDto(createdAt, createdBy, modifiedAt, modifiedBy, userAccountDto, userPostId, title, postContent, hidePost, postLevel, postView, userPostCommentDtos);
    }


    public static UserPostWithCommentDto from(UserPost entity){
        return  new UserPostWithCommentDto(
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getUserPostId(),
                entity.getTitle(),
                entity.getPostContent(),
                entity.getHidePost(),
                entity.getPostLevel(),
                entity.getPostView(),
                //아 헷갈린다
                entity.getUserPostComments().stream().map(UserPostCommentDto::from).collect(Collectors.toCollection(LinkedHashSet::new))

        );
    }

}
