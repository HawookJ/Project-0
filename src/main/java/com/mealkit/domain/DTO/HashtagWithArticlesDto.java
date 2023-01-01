package com.mealkit.domain.DTO;


import com.mealkit.domain.Hashtag;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record HashtagWithArticlesDto(
        Long id,
        Set<AdminPostDto> adminPostDtos,
        String hashtagName,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static HashtagWithArticlesDto of(Set<AdminPostDto> adminPostDtos, String hashtagName) {
        return new HashtagWithArticlesDto(null, adminPostDtos, hashtagName, null, null, null, null);
    }

    public static HashtagWithArticlesDto of(Long id, Set<AdminPostDto> adminPostDtos, String hashtagName, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new HashtagWithArticlesDto(id, adminPostDtos, hashtagName, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static HashtagWithArticlesDto from(Hashtag entity) {
        return new HashtagWithArticlesDto(
                entity.getId(),
                entity.getAdminPosts().stream()
                        .map(AdminPostDto::from)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                entity.getHashtagName(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Hashtag toEntity() {
        return Hashtag.of(hashtagName);
    }

}