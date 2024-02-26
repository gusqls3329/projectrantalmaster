package com.team5.projrental.entities.enums;

import lombok.Getter;

@Getter
public enum BoardStatus {
    ACTIVATED(0), DELETED(-1);

    private int num;

    BoardStatus(int num) {
    this.num = num;
    }


}
