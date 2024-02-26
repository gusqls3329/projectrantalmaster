package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.Getter;

import java.util.Arrays;

//- ACTIVED(1) : 활동중 ,
//- DELETED(-1): 벌점누적으로 정지됨 ,
//- HIDDEN(999) : 탈퇴유저,
//- FAILED(-2) : 회원가입 반려됨
@Getter
public enum UserStatus {
    ACTIVATED(1), DELETED(-1), HIDDEN(999), FAILED(-2);
    private int code;
    UserStatus(int code) {
        this.code = code;
    }

    public static UserStatus getByNum(int num) {
        return Arrays.stream(UserStatus.values()).filter(us -> us.getCode() == num)
                .findFirst().orElseThrow(() -> new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE,
                        "잘못된 상태코드 입니다.(status)"));
    }
}
