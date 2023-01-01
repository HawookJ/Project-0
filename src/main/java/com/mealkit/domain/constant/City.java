package com.mealkit.domain.constant;

import lombok.Getter;

public enum City {

    SEOUL("서울"),
    INCHEON("인천"),
    BUSAN("부산"),
    GYEONGGI("경기");






    @Getter
    private final String description;

    City(String description) {
        this.description = description;
    }
}
