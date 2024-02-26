package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DisputeByAdminVo {

    private Long totalDisputeCount;
    private List<DisputeInfoByAdmin> disputes;

}
