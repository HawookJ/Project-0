package com.mealkit.domain.post.admin.Dto.request;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;

import java.util.Set;

public record   AdminPostRequest(
        String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
        double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
        String homeMeal, Board board, Long homeView, String homeDetails,
        String homeTitle
) {

    public static AdminPostRequest of( String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
                                       double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                                       String homeMeal, Board board, Long homeView, String homeDetails,
                                       String homeTitle) {
        return new AdminPostRequest(homeName, homeAddress, homeNumber, homeCCTV, homeSize, homeChildren, homeRegister,
                homeVideo, homeMeal, board, homeView, homeDetails, homeTitle);
    }

    public AdminPostDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public AdminPostDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return AdminPostDto.of(
                userAccountDto, hashtagDtos, homeName, homeAddress, homeNumber, homeCCTV,
                homeSize, homeChildren, homeRegister, homeVideo, homeMeal, board, homeView, homeDetails,
                homeTitle


        );
    }

}