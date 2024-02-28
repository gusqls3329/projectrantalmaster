package com.team5.projrental.entities.enums;

import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ServerException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BoardStatus {
    ACTIVATED(0), DELETED(-1);

    private int num;

    BoardStatus(int num) {
    this.num = num;
    }

    public static BoardStatus getByNum(int num) {
        return Arrays.stream(BoardStatus.values())
                .filter(o -> o.num == num)
                .findFirst()
                .orElseThrow(() -> new ServerException(ErrorCode.SERVER_ERR_MESSAGE, "ㅇㅇㅇㅇ"));
    }


}
