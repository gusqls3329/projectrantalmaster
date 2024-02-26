package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 삭제 사유
 */
@Getter
public enum DisputeReason {
    // todo 점수 제대로 책정
    DELETE_BY_ADMIN(0, 0),
    // 분쟁
    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1, -10),
    CAN_NOT_CONTACT_AFTER_ACTIVATE(2, 999),
    FALSE(3, 999),
    DIF_PRODUCT(4, 999),
    MANNER(5, 999),
    LATE(6, 999),

    // 사고
    DAMAGED(-1, -20),
    LOSE(-2, 999);

    private int num;
    private int penaltyScore;

    DisputeReason(int num, int penaltyScore) {
        this.num = num;
        this.penaltyScore = penaltyScore;
    }

    public static DisputeReason getByNum(int num) {
        return Arrays.stream(DisputeReason.values()).filter(r -> r.getNum() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "존재하지 않는 코드입니다."));
    }

    public static List<DisputeReason> getByDiv(int div) {
        return Arrays.stream(DisputeReason.values()).map(dr -> {
            if (div < 0) {
                if (dr.getNum() < 0) return dr;
            }
            if (div > 0) {
                if (dr.getNum() > 0) return dr;
            }
            throw new ClientException(ErrorCode.BAD_DIV_INFO_EX_MESSAGE, "잘못된 div 값");
        }).toList();
    }
}
