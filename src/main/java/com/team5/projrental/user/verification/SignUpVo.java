package com.team5.projrental.user.verification;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpVo {
    private Integer result;
}
