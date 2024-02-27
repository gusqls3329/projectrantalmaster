package com.team5.projrental.board.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardPicInsDto {
    private int iboard;
    private List<String> storedPic;
}
