package com.mealkit.domain.post.user.Dto.response;

import com.mealkit.domain.post.user.Dto.UserPostDto;
import com.mealkit.domain.post.user.PostContent;

import java.time.LocalDateTime;

public record UserPostResponse(
        Long postId,
        String title,
        PostContent postContent,
        Integer postLevel,
        Integer hidePost,
        Long postView,
        String hashtag,
        String nickName,
        LocalDateTime createdAt) {
    public static UserPostResponse of(Long postId,
                                      String title,
                                      PostContent postContent,
                                      Integer postLevel,
                                      Integer hidePost,
                                      Long postView,
                                      String hashtag,
                                      String nickName,
                                      LocalDateTime createdAt) {
        return new UserPostResponse(postId, title, postContent, postLevel, hidePost, postView, hashtag, nickName, createdAt);
    }

    public static UserPostResponse from(UserPostDto dto){
    String nickName = dto.userAccountDto().nickName();
    if(nickName==null || nickName.isBlank()){
        nickName= dto.userAccountDto().userEmail();
    }
    return new UserPostResponse(
            dto.postId(),
            dto.title(),
            dto.postContent(),
            dto.postLevel(),
            dto.hidePost(),
            dto.postView(),
            dto.userAccountDto().userEmail(),
            nickName,
            dto.createdAt()
    );
    }
}
