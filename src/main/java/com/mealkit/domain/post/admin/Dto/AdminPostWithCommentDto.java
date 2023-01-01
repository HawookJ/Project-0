package com.mealkit.domain.post.admin.Dto;

import com.mealkit.domain.Board;
import com.mealkit.domain.DTO.HashtagDto;
import com.mealkit.domain.DTO.UserAccountDto;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.AdminPostComment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.mealkit.domain.post.admin.AdminPost} entity
 */
public record AdminPostWithCommentDto(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
                                      String modifiedBy, UserAccountDto userAccountDto, Long homeId, String homeName, String homeAddress,
                                      String homeNumber, boolean homeCCTV, double homeSize, Integer homeChildren,
                                      String homeRegister, String homeVideo, String homeMeal, Board board,
                                      Long homeView, String homeDetails, String homeTitle,  Set<HashtagDto> hashtagDtos,
                                      Set<AdminPostCommentDto> adminPostCommentDtos) {


public static AdminPostWithCommentDto of(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt,
                                         String modifiedBy,UserAccountDto userAccountDto, Long homeId, String homeName, String homeAddress,
                                         String homeNumber, boolean homeCCTV, double homeSize, Integer homeChildren,
                                         String homeRegister, String homeVideo, String homeMeal, Board board,
                                         Long homeView, String homeDetails, String homeTitle,  Set<HashtagDto> hashtagDtos,
                                         Set<AdminPostCommentDto> adminPostCommentDtos){

    return new AdminPostWithCommentDto(createdAt, createdBy, modifiedAt, modifiedBy, userAccountDto, homeId, homeName, homeAddress, homeNumber, homeCCTV, homeSize,
            homeChildren, homeRegister, homeVideo, homeMeal, board, homeView, homeDetails, homeTitle, hashtagDtos ,adminPostCommentDtos);
}

public static AdminPostWithCommentDto from(AdminPost entity){
    return new AdminPostWithCommentDto(
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getModifiedAt(),
            entity.getModifiedBy(),
            UserAccountDto.from(entity.getUserAccount()),
            entity.getHomeId(),
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
            entity.getHashtags().stream()
                    .map(HashtagDto::from)
                    .collect(Collectors.toUnmodifiableSet())
            ,
            entity.getAdminPostComments().stream().map(AdminPostCommentDto::from).collect(Collectors.toCollection(LinkedHashSet::new)));



}

}