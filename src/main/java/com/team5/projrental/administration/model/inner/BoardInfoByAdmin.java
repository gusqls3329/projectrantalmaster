package com.team5.projrental.administration.model.inner;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BoardInfoByAdmin {
    private Long iboard;
    private String title;
    private String nick;
    private Long view;
    private LocalDateTime createdAt;
}
