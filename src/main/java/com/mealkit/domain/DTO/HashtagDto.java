package com.mealkit.domain.DTO;

import com.mealkit.domain.Hashtag;
import com.mealkit.domain.post.admin.AdminPost;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link com.mealkit.domain.Hashtag} entity
 */
public record HashtagDto(LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy,
                         Long id, String hashtagName)  {

    public static HashtagDto of(String hashtagName) {
        return new HashtagDto(null, null, null, null, null, hashtagName);
    }

    public static HashtagDto of(Long id, String hashtagName, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new HashtagDto(createdAt, createdBy, modifiedAt, modifiedBy,id, hashtagName);
    }

    public static HashtagDto from(Hashtag entity) {
        return new HashtagDto(
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                entity.getId(),
                entity.getHashtagName()

        );
    }

    public Hashtag toEntity() {
        return Hashtag.of(hashtagName);
    }

}