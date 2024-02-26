package com.team5.projrental.payment.thirdproj.paymentinfo;

import com.team5.projrental.entities.PaymentInfo;
import com.team5.projrental.entities.ids.PaymentInfoIds;

import java.util.Optional;

public interface PaymentInfoQueryRepository {
    Optional<PaymentInfo> findByIdJoinFetchPaymentInfoAndProductBy(PaymentInfoIds ids);

    Optional<PaymentInfo> findJoinFetchPaymentByCode(String code);

    Optional<PaymentInfo> findJoinFetchPaymentProductByUser(String code);

}
