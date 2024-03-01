package com.team5.projrental.board.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListVo {
    private int totalBoardCount;
    private List<BoardListSelVo> boardList;
}
