package com.team5.projrental.mypage;

import com.team5.projrental.entities.Dispute;

import java.util.List;

public interface MyPageDisputeQueryRepository {
    List<Dispute> getDisputeList(Long loginUserPk);
}
