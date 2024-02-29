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
    DELETE_BY_ADMIN(0, 0, "운영자에 의해 삭제됨"),
    // 분쟁
    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1, -15, "연락 두절 (거래 활성화 전)"),
    CAN_NOT_CONTACT_AFTER_ACTIVATE(2, -15, "연락 두절 (거래 활성화 후)"),
    FALSE(3, -10, "잘못된 정보"),
    DIF_PRODUCT(4, -30, "반납된 제품이 다름"),
    MANNER(5, -5, "비매너 행위"),
    LATE(6, -10, "반납이 늦음"),

    // 사고
    DAMAGED(-1, -20, "제품 파손 및 손상"),
    LOSE(-2, -30, "제품 분실"),;

    private String word;
    private int num;
    private int penaltyScore;

    DisputeReason(int num, int penaltyScore, String word) {
        this.num = num;
        this.penaltyScore = penaltyScore;
        this.word = word;
    }

    public static DisputeReason getByNum(int num) {
        return Arrays.stream(DisputeReason.values()).filter(r -> r.getNum() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "존재하지 않는 코드입니다."));
    }

    public static List<DisputeReason> getByDiv(int div) {

        return Arrays.stream(DisputeReason.values())
                .filter(dr -> div < 0 && dr.getNum() < 0 || div > 0 && dr.getNum() > 0)
                .toList();
    }
}
