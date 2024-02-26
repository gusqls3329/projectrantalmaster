package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.BoardInfoByAdmin;
import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardByAdminVo {

    private Long totalBoardCount;
    private List<BoardInfoByAdmin> boards;


}
