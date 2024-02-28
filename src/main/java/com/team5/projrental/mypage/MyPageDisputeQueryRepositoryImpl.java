package com.team5.projrental.mypage;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.entities.Dispute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.team5.projrental.entities.QDispute.dispute;
import static com.team5.projrental.entities.QDisputeBoard.disputeBoard;
import static com.team5.projrental.entities.QDisputeChatUser.disputeChatUser;
import static com.team5.projrental.entities.QDisputePayment.disputePayment;
import static com.team5.projrental.entities.QDisputeProduct.disputeProduct;
import static com.team5.projrental.entities.QDisputeUser.disputeUser;
import static com.team5.projrental.entities.QUser.user;


@Slf4j
@RequiredArgsConstructor
public class MyPageDisputeQueryRepositoryImpl implements MyPageDisputeQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Dispute> getDisputeList(Long loginUserPk) {
       // JPAQuery<Dispute> jpaQuery = jpaQueryFactory.selectFrom(dispute)


        return null;//jpaQuery.fetch();

    }
}
