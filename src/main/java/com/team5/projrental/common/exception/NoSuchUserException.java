package com.team5.projrental.common.exception;

import com.team5.projrental.common.exception.base.NoSuchDataException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class NoSuchUserException extends NoSuchDataException {
    private ErrorCode errorCode;
    public NoSuchUserException(String message) {
        super(message);
        this.errorCode = Arrays.stream(ErrorCode.values()).filter(e -> e.getMessage().equals(message)).findFirst()
                .orElse(ErrorCode.SERVER_ERR_MESSAGE);
    }

    public NoSuchUserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}