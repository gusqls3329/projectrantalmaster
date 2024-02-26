package com.team5.projrental.payment;

import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.payment.model.PaymentInsDto;
import com.team5.projrental.payment.model.PaymentVo;

public interface RefPaymentService {

    ResVo postPayment(PaymentInsDto paymentInsDto);

    ResVo delPayment(Long ipayment, Integer div);

    PaymentVo getPaymentInfo(Long ipayment);

}
