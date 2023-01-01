package com.mealkit.domain.post.user.Dto.response;

import com.mealkit.domain.post.user.Dto.UserPostCommentDto;
import com.mealkit.domain.post.user.PostContent;
import com.mealkit.domain.post.user.Dto.UserPostWithCommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public record UserPostWithCommentResponse(
        Long commentId,
        String title,
        PostContent postContent,
        Integer postLevel,
        Integer hidePost,
        Long postView,
        String userEmail,
        String nickName,
        LocalDateTime createdAt,
        Set<UserPostCommentResponse> userPostCommentDtos) {

    public static UserPostWithCommentResponse of(Long commentId,
                                                 String title,
                                                 PostContent postContent,
                                                 Integer postLevel,
                                                 Integer hidePost,
                                                 Long postView,
                                                 String userEmail,
                                                 String nickName,
                                                 LocalDateTime createdAt,
                                                 Set<UserPostCommentResponse> userPostCommentDtos) {
         return new UserPostWithCommentResponse(commentId, title, postContent, postLevel, hidePost, postView, userEmail, nickName, createdAt, userPostCommentDtos);
    }

    public static UserPostWithCommentResponse from(UserPostWithCommentDto dto){
        String nickName = dto.userAccountDto().nickName();
        if(nickName == null || nickName.isBlank()){
            nickName = dto.userAccountDto().userEmail();
        }
        return new UserPostWithCommentResponse(
                dto.userPostId(),
                dto.title(),
                dto.postContent(),
                dto.postLevel(),
                dto.hidePost(),
                dto.postView(),
                dto.userAccountDto().userEmail(),
                nickName,
                dto.createdAt(),
                organizeChildComments(dto.userPostCommentDtos())




        );
    }

    private static Set<UserPostCommentResponse> organizeChildComments(Set<UserPostCommentDto> dtos) {
        Map<Long, UserPostCommentResponse> map = dtos.stream()
                .map(UserPostCommentResponse::from)
                .collect(Collectors.toMap(UserPostCommentResponse::commentId, Function.identity()));

        map.values().stream()
                .filter(UserPostCommentResponse::hasParentComment)
                .forEach(comment -> {
                    UserPostCommentResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(UserPostCommentResponse::createdAt)
                                .reversed()
                                .thenComparingLong(UserPostCommentResponse::commentId)
                        )
                ));
    }



}
