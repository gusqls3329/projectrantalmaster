package com.team5.projrental.mypage;

import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.mypage.model.MyDisputeVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DisputeByMyPageVo {
    private Long totalUserCount;
    private List<MyDisputeVo> disputes;
}
