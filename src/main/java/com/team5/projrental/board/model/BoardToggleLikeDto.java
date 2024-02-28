package com.team5.projrental.board.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardToggleLikeDto {
    private long iboard;
    private long loginIuser;
    private LocalDateTime createdAt = LocalDateTime.now();
}
