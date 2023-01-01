package com.mealkit.domain.DTO;


import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.RoleType;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link UserAccount} entity
 */
public record UserAccountDto(LocalDateTime createdAt, LocalDateTime modifiedAt,
                             Long userId, String userName, String nickName, String userEmail, String userChild,
                             Integer userLevel, String userMemo,
                             String userPassword) {


    public static UserAccountDto of(Long userId, String userName, String nickName, String userEmail, String userChild,
                                    Integer userLevel, String userMemo,
                                    String userPassword) {
        return new UserAccountDto(null, null, userId, userName, nickName,
                userEmail, userChild, userLevel, userMemo, userPassword);
    }

    public static UserAccountDto of(LocalDateTime createdAt, LocalDateTime modifiedAt,
                                    Long userId, String userName, String nickName, String userEmail, String userChild,
                                    Integer userLevel, String userMemo,
                                    String userPassword) {
        return new UserAccountDto(createdAt, modifiedAt, userId, userName, nickName, userEmail, userChild,
                userLevel, userMemo, userPassword);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getUserId(),
                entity.getUserName(),
                entity.getNickName(),
                entity.getUserEmail(),
                entity.getUserChild(),
                entity.getUserLevel(),
                entity.getUserMemo(),
                entity.getUserPassword());

    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userId,userName,userLevel,userEmail,userChild,userPassword,nickName,userMemo, "provider", RoleType.USER
        );
    }
}