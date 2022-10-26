package com.mealkit.domain.constant;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum BoardName {

    forProvider(1, "어린이집"),
    forUser(2, "게시글");

    private final int value;
    private final String name;

private BoardName(int value, String name){
    this.value=value;
    this.name=name;
}

public static BoardName getBoardNameByNo(int boardNo){
    for(BoardName b :BoardName.values()){
        if(b.value==boardNo){
            return b;
        }
    }
    return null;
}


}
