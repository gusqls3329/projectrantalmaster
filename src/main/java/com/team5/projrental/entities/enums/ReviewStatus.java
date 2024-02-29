package com.team5.projrental.entities.enums;


import lombok.Getter;

/**
 * 리뷰 상태
 */
@Getter
public enum ReviewStatus {
    ACTIVATED(1), DELETED(-1);

    private int code;
    ReviewStatus(int code) {
        this.code = code;
    }
}

