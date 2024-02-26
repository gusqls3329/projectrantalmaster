package com.team5.projrental.entities.enums;

import lombok.Getter;

// 만료(날짜지남) : EXPIRED, 거래완료 : COMPLETED, 삭제됨: DELETE
@Getter
public enum PaymentInfoStatus {

    EXPIRED(-4), ACTIVATED(0), COMPLETED(1), RESERVED(2), DELETED(-1), HIDDEN(-2), CANCELED(-3);

    private int num;
    PaymentInfoStatus(int num) {
        this.num = num;
    }


}
