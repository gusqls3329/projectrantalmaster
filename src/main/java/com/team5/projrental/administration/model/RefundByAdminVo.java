package com.team5.projrental.administration.model;

import com.team5.projrental.administration.model.inner.DisputeInfoByAdmin;
import com.team5.projrental.administration.model.inner.RefundInfoByAdmin;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RefundByAdminVo {
    private Long totalRefundCount;
    private List<RefundInfoByAdmin> refunds;
}
