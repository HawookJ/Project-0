package com.mealkit.domain.post.admin.Dto;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.admin.AdminPost;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.mealkit.domain.post.admin.AdminPost} entity
 */
public record AdminPostDto(Long homeId, UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos,
                            String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
                           double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                           String homeMeal, Board board, Long homeView, String homeDetails,
                           String homeTitle, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {




    public static AdminPostDto of(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos,
                                  String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
                                  double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                                  String homeMeal, Board board, Long homeView, String homeDetails,
                                  String homeTitle) {
        return new AdminPostDto(null, userAccountDto ,hashtagDtos, homeName, homeAddress, homeNumber, homeCCTV, homeSize, homeChildren, homeRegister, homeVideo,
                homeMeal, board, homeView, homeDetails, homeTitle ,null,null,null,null);
    }

    public static AdminPostDto of( Long homeId, UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos,
                                   String homeName, String homeAddress, String homeNumber, boolean homeCCTV,
                                   double homeSize, Integer homeChildren, String homeRegister, String homeVideo,
                                   String homeMeal, Board board, Long homeView, String homeDetails,
                                   String homeTitle, LocalDateTime createdAt, String createdBy,
                                   LocalDateTime modifiedAt, String modifiedBy) {
        return new AdminPostDto( homeId, userAccountDto,hashtagDtos, homeName, homeAddress, homeNumber, homeCCTV, homeSize, homeChildren,
                homeRegister, homeVideo, homeMeal, board, homeView, homeDetails, homeTitle, createdAt, createdBy, modifiedAt, modifiedBy);
    }
    public static AdminPostDto from(AdminPost entity) {
        return new AdminPostDto(
                entity.getHomeId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getHomeName(),
                entity.getHomeAddress(),
                entity.getHomeNumber(),
                entity.isHomeCCTV(),
                entity.getHomeSize(),
                entity.getHomeChildren(),
                entity.getHomeRegister(),
                entity.getHomeVideo(),
                entity.getHomeMeal(),
                entity.getBoard(),
                entity.getHomeView(),
                entity.getHomeDetails(),
                entity.getHomeTitle(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy());
    }

    public AdminPost toEntity(UserAccount userAccount) {
        return AdminPost.of(
                userAccount, homeName,homeAddress,homeNumber,homeCCTV,homeSize,homeChildren,homeRegister,homeVideo,homeMeal,homeView,homeDetails,
                homeTitle
        );
    }



}