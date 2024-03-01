package com.team5.projrental.payment.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.exception.thrid.ServerException;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.entities.PaymentDetail;
import com.team5.projrental.entities.enums.PaymentDetailCategory;
import com.team5.projrental.payment.kakao.model.logic.PayApproveResponseDto;
import com.team5.projrental.payment.kakao.model.logic.PayApproveVo;
import com.team5.projrental.payment.kakao.model.logic.PayReadyResponseDto;
import com.team5.projrental.payment.kakao.model.logic.PayReadyVo;
import com.team5.projrental.payment.kakao.model.ready.PayInfoDto;
import com.team5.projrental.payment.kakao.model.ready.PayReadyDto;
import com.team5.projrental.payment.kakao.model.success.PayApproveBodyInfo;
import com.team5.projrental.payment.kakao.model.success.PayApproveDto;
import com.team5.projrental.payment.kakao.property.ApiPayProperty;
import com.team5.projrental.payment.kakao.requester.KakaoPayRequestGenerator;
import com.team5.projrental.payment.thirdproj.paymentdetail.PaymentDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final KakaoPayRequestGenerator requester;
    private final AuthenticationFacade facade;
    private final ApiPayProperty property;
    private final ObjectMapper om;

    private final PaymentDetailRepository repository;

    @Transactional
    public PayReadyVo ready(PayInfoDto dto) {
        int betweenDay = ((int) ChronoUnit.DAYS.between(dto.getRentalStartDate(), dto.getRentalEndDate())) + 1;
        // 전체 금액 계산
        dto.setTotalPrice(betweenDay * dto.getPricePerDay());

        // deposit 계산
        dto.setDeposit((int) ((100 + (betweenDay * 10)) * 0.01) * dto.getPricePerDay());

        PayReadyDto payReadyDto = requester.getReadyRequest(facade.getLoginUserPk(), dto);

        String response = RestClient.builder()
                .baseUrl(payReadyDto.getRequestUrl())
                .build()
                .post()
                .header(property.getHeaderKey(), property.getHeaderValue())
                .body(payReadyDto.getPayReadyBodyInfo())
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        PayReadyResponseDto responseDto;

        try {
            responseDto = om.readValue(response, PayReadyResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ServerException(ErrorCode.SERVER_ERR_MESSAGE, "Api 통신과정에서 에러가 발생했습니다.");
        }

        PaymentDetail paymentDetail = null;

        if (responseDto.getTid() != null && !responseDto.getTid().isEmpty()) {
            paymentDetail = PaymentDetail.builder()
                    .tid(responseDto.getTid())
                    .category(PaymentDetailCategory.KAKAO_PAY)
                    .build();
            repository.save(paymentDetail);
        }

        return PayReadyVo.builder()
                .id(paymentDetail == null ? null : paymentDetail.getId())
                .nextRequestUrl(responseDto.getNext_redirect_pc_url())
                .build();
    }

    @Transactional
    public PayApproveVo approve(String pgToken, Long id) {

        PaymentDetail findPaymentDetail = repository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.NO_SUCH_PAYMENT_EX_MESSAGE,
                        "잘못된 결제 정보 입니다.")
                );

        String tid = findPaymentDetail.getTid();

        PayApproveDto approveRequest = requester.getApproveRequest(tid, facade.getLoginUserPk(), pgToken);

        PayApproveBodyInfo payApproveBodyInfo = approveRequest.getPayApproveBodyInfo();

        String response = RestClient.builder()
                .baseUrl(approveRequest.getRequestUrl())
                .build()
                .post()
                .header(property.getHeaderKey(), property.getHeaderValue())
                .body(payApproveBodyInfo)
                .contentType(MediaType.APPLICATION_JSON).retrieve().body(String.class);

        PayApproveResponseDto payApproveResponseDto;

        try {
            payApproveResponseDto = om.readValue(response, PayApproveResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ClientException(ErrorCode.BAD_INFO_EX_MESSAGE);
        }

        PayApproveVo result = PayApproveVo.builder()
                .totalPrice(payApproveResponseDto.getAmount().getTotal())
                .tax(payApproveResponseDto.getAmount().getTax())
                .itemName(payApproveResponseDto.getItem_name())
                .createdAt(payApproveResponseDto.getCreated_at())
                .approvedAt(payApproveResponseDto.getApproved_at())
                .id(id)
                .build();

        findPaymentDetail.setAmount(result.getTotalPrice());

        return result;

    }
}
