package com.mealkit.domain.post.user.Dto.request;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.user.Dto.UserPostDto;
import com.mealkit.domain.post.user.PostContent;

import java.util.Set;

public record UserPostRequest(

        Board board,
        String title,
        PostContent postContent,
        Integer hidePost,
        Integer postLevel,
        Long postView
) {


    public static UserPostRequest of(
            Board board,
            String title,
            PostContent postContent,
            Integer hidePost,
            Integer postLevel,
            Long postView) {
        return new UserPostRequest(
                board,
                title,
                postContent,
                hidePost,
                postLevel,
                postView);
    }

    public UserPostDto toDto(UserAccountDto userAccountDto) {
        return toDTo(userAccountDto, null);
    }

    public UserPostDto toDTo(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return UserPostDto.of(
                title,
                postContent,
                hidePost,
                postLevel,
                postView,
                userAccountDto,
                hashtagDtos,
                board);
    }


}
