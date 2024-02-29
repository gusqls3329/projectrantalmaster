package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DisputeStatus {
    ACCEPTED(1, "수리됨"), STAND_BY(0, "대기중"), DENIED(-1,  "반려됨"), CANCELED(-2, "취소됨");

    private String word;
    private int num;

    DisputeStatus(int num, String word) {
        this.num = num;
        this.word = word;
    }

    public static DisputeStatus getByNum(int num) {
        return Arrays.stream(DisputeStatus.values()).filter(ds -> ds.num == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 값을 제공하였습니다 (type)"));

    }
}
