package com.team5.projrental.common.sse.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SseCode {
    BOARD_DELETED_BY_ADMIN(2), USER_DELETED_BY_ADMIN(3), PRODUCT_DELETE_BY_ADMIN(5),
    DISPUTE_IS_ACCEPTED(1), DISPUTE_IS_DENIED(-1);


    private int num;

    SseCode(int num) {
        this.num = num;
    }

    public static SseCode getByNum(int num) {
        return Arrays.stream(SseCode.values()).filter(code -> code.getNum() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 코드 (code)"));

    }


}
