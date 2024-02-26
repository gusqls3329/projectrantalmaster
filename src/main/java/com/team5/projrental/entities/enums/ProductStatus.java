package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ServerException;

import java.util.Arrays;

public enum ProductStatus {
    ACTIVATED(1), HIDDEN (-2), DELETED (-1), PRODUCT_OF_UNREGISTERED_USER(999);

    private int num;
    ProductStatus(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public static ProductStatus getByNum(int num) {
        return Arrays.stream(ProductStatus.values())
                .filter(o -> o.num == num)
                .findFirst()
                .orElseThrow(() -> new ServerException(ErrorCode.SERVER_ERR_MESSAGE, "제품의 상태가 정상적이지 않습니다."));
    }
}
