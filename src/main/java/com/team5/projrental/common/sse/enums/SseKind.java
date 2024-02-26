package com.team5.projrental.common.sse.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SseKind {
    REVIEW(1), PAYMENT(2), DISPUTE(3), BOARD(4), USER(5),
    PRODUCT(6);
    private int num;

    SseKind(int num) {
        this.num = num;
    }


    public static SseKind getByNum(int num) {
        return Arrays.stream(SseKind.values()).filter(code -> code.getNum() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 종류 (kind)"));

    }
}
