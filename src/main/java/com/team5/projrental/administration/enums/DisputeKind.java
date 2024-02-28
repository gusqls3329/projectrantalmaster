package com.team5.projrental.administration.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.entities.enums.DisputeReason;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DisputeKind {
    BOARD(1), CHAT(2), PAYMENT(3), PRODUCT(4);

    private int num;

    DisputeKind(int num) {
        this.num = num;
    }
    public static DisputeReason getByNum(int num) {
        return Arrays.stream(DisputeReason.values()).filter(r -> r.getNum() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "존재하지 않는 코드입니다."));
    }
}
