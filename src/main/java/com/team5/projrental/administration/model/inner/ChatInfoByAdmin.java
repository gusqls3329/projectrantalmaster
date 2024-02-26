package com.team5.projrental.administration.model.inner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatInfoByAdmin {

    private Long ichat;
    private String category;
    private String nick;
    private LocalDateTime createdAt;

}
