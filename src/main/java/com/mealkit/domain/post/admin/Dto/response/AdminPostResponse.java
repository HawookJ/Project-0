package com.mealkit.domain.post.admin.Dto.response;


import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record AdminPostResponse(
        Long homeId,
        String homeTitle,
        String homeDetails,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String userEmail,
        String homeName,
        String homeAddress,
        String homeNumber,
        boolean homeCCTV,
        double homeSize,
        Integer homeChildren,
        String homeRegister,
        String homeMeal,
        String homeVideo,
        String nickName
) {

    public static AdminPostResponse of(        Long homeId,
                                               String homeTitle,
                                               String homeDetails,
                                               Set<String> hashtags,
                                               LocalDateTime createdAt,
                                               String userEmail,
                                               String homeName,
                                               String homeAddress,
                                               String homeNumber,
                                               boolean homeCCTV,
                                               double homeSize,
                                               Integer homeChildren,
                                               String homeRegister,
                                               String homeMeal,
                                               String homeVideo,
                                               String nickName) {
        return new AdminPostResponse(homeId, homeTitle, homeDetails, hashtags, createdAt, userEmail, homeName, homeAddress, homeNumber, homeCCTV, homeSize, homeChildren, homeRegister, homeMeal, homeVideo, nickName);
    }

    public static AdminPostResponse from(AdminPostDto dto) {
        String nickname = dto.userAccountDto().nickName();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userEmail();
        }

        return new AdminPostResponse(
                dto.homeId(),
                dto.homeTitle(),
                dto.homeDetails(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                dto.createdAt(),
                dto.userAccountDto().userEmail(),
                dto.homeName(),
                dto.homeAddress(),
                dto.homeNumber(),
                dto.homeCCTV(),
                dto.homeSize(),
                dto.homeChildren(),
                dto.homeRegister(),
                dto.homeMeal(),
                dto.homeVideo(),
                nickname
        );
    }

}