package com.mealkit.domain.post.admin.Dto.response;

import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;
import com.mealkit.domain.post.admin.Dto.AdminPostWithCommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public record AdminPostWithCommentResponse
        (
    Long homeId,
    String homeTitle,
    String homeDetails,
    LocalDateTime createdAt,
    String homeVideo,
    String userEmail,
    String nickName,
    Set<String> hashtags,
    Set<AdminPostCommentResponse> adminPostCommentResponse
        )
{
public static AdminPostWithCommentResponse of(    Long homeId,  String homeTitle,String homeDetails, LocalDateTime createdAt,String homeVideo,String userEmail,
                                                  String nickName,   Set<String> hashtags,   Set<AdminPostCommentResponse> adminPostCommentResponse){
        return new AdminPostWithCommentResponse(homeId,homeTitle, homeDetails, createdAt, homeVideo, userEmail,nickName,hashtags,adminPostCommentResponse);
}

public static AdminPostWithCommentResponse from(AdminPostWithCommentDto dto) {
        String nickName = dto.userAccountDto().nickName();
        if (nickName == null || nickName.isBlank()) {
                nickName = dto.userAccountDto().userEmail();


        }

        return new AdminPostWithCommentResponse(
                dto.homeId(),
                dto.homeTitle(),
                dto.homeDetails(),
                dto.createdAt(),
                dto.homeVideo(),
                dto.userAccountDto().userEmail(),

                nickName,
                dto.hashtagDtos().stream().map(HashtagDto::hashtagName).collect(Collectors.toUnmodifiableSet()),
                organizeChildComments(dto.adminPostCommentDtos()));
}

        private static Set<AdminPostCommentResponse> organizeChildComments(Set<AdminPostCommentDto> dtos) {
                Map<Long, AdminPostCommentResponse> map = dtos.stream()
                        .map(AdminPostCommentResponse::from)
                        .collect(Collectors.toMap(AdminPostCommentResponse::adminPostCommentId, Function.identity()));

                map.values().stream()
                        .filter(AdminPostCommentResponse::hasParentComment)
                        .forEach(comment -> {
                                AdminPostCommentResponse parentComment = map.get(comment.parentCommentId());
                                parentComment.childComments().add(comment);
                        });

                return map.values().stream()
                        .filter(comment -> !comment.hasParentComment())
                        .collect(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator
                                        .comparing(AdminPostCommentResponse::createdAt)
                                        .reversed()
                                        .thenComparingLong(AdminPostCommentResponse::adminPostCommentId)
                                )
                        ));
        }



}
