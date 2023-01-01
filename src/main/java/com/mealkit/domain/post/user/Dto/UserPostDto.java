package com.mealkit.domain.post.user.Dto;


import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.post.user.PostContent;
import com.mealkit.domain.post.user.UserPost;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record UserPostDto (
        Long postId,
        String title,
        PostContent postContent,
        Integer postLevel,
        Integer hidePost,
        Long postView,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,
        UserAccountDto userAccountDto,
        Set<HashtagDto> hashtagDtos,
        Board board
){
public static UserPostDto of(
                                     String title,
                                     PostContent postContent,
                                     Integer postLevel,
                                     Integer hidePost,
                                     Long postView,
                                     UserAccountDto userAccountDto,
                                     Set<HashtagDto> hashtagDtos,
                                     Board board)
{
    return new UserPostDto(null, title, postContent, postLevel, hidePost, postView,null, null, null, null, userAccountDto, hashtagDtos, board);
}

    public static UserPostDto of(      Long postId,
                                       String title,
                                       PostContent postContent,
                                       Integer postLevel,
                                       Integer hidePost,
                                       Long postView,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy,
                                       UserAccountDto userAccountDto,
                                       Set<HashtagDto> hashtagDtos,
                                       Board board){
    return new UserPostDto(postId, title, postContent, postLevel, hidePost, postView, createdAt, createdBy, modifiedAt, modifiedBy, userAccountDto, hashtagDtos, board);
    }
    public static UserPostDto from(UserPost entity){
    return new UserPostDto(
            entity.getUserPostId(),

            entity.getTitle(),
            entity.getPostContent(),
            entity.getPostLevel(),
            entity.getHidePost(),
            entity.getPostView(),
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getModifiedAt(),
            entity.getModifiedBy(),
            UserAccountDto.from(entity.getUserAccount()),
            entity.getHashtags().stream()
                    .map(HashtagDto::from)
                    .collect(Collectors.toUnmodifiableSet()),
            entity.getBoard()

    );

    }
    public UserPost toEntity(UserAccount userAccount){
        return UserPost.of(
                userAccount,
                title,
                postContent,
                hidePost,
                postLevel,
                postView
        );
    }

}
