package com.team5.projrental.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResultVo {
    private String message;
    private Integer errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason;

}
