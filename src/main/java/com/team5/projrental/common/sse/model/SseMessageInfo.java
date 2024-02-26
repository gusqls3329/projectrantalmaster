package com.team5.projrental.common.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SseMessageInfo {
    private Long receiver;
    private String description;
    private int code;
    private Long identityNum;
    private Integer kind;

}
