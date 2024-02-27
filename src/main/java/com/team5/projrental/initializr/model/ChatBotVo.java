package com.team5.projrental.initializr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatBotVo {

    private Integer grp;
    private Integer level;
    private Integer depth;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mention;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String redirectUrl;

}
