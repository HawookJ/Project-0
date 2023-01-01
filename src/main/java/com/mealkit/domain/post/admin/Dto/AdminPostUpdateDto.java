package com.mealkit.domain.post.admin.Dto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.mealkit.domain.post.admin.AdminPost} entity
 */
public record AdminPostUpdateDto(LocalDateTime modifiedAt, String modifiedBy, String homeName, String homeAddress,
                                 String homeNumber, boolean CCTV, double homeSize, Integer homeChildren, String homeRegister,
                                 String homeVideo, String homeMeal, Long homeView, String homeDetails,
                                 String homeTitle)  {
    public static AdminPostUpdateDto of(LocalDateTime modifiedAt, String modifiedBy, String homeName, String homeAddress,
                                        String homeNumber, boolean CCTV, double homeSize, Integer homeChildren, String homeRegister,
                                        String homeVideo, String homeMeal, Long homeView, String homeDetails,
                                        String homeTitle )
    {
        return new AdminPostUpdateDto(modifiedAt, modifiedBy, homeName, homeAddress, homeNumber, CCTV, homeSize, homeChildren, homeRegister, homeVideo, homeMeal, homeView, homeDetails, homeTitle);
    }
}