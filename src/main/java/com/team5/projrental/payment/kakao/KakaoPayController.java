package com.team5.projrental.payment.kakao;

import com.team5.projrental.payment.kakao.model.logic.PayApproveVo;
import com.team5.projrental.payment.kakao.model.logic.PayReadyVo;
import com.team5.projrental.payment.kakao.model.ready.PayInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.DependentRequiredMap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/pay/kakao")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @Operation(summary = "카카오페이 결제 준비 요청",
            description = "전체 필드 모두 필수<br>" +
                          "itemName: 결제 대상 물건 명<br>" +
                          "pricePerDay: 1일당 대여 금액<br>" +
                          "rentalStartDate: 거래 시작일<br>" +
                          "rentalEndDate: 거래 종료일<br><br>" +
                          "<응답> <br>" +
                          "성공시: <br>" +
                          "nextRequestUrl: 다음 리다이렉트 경로, pg_token 은 따로 추출해서 다음 백엔드 서버로 요청시 제공<br>" +
                          "id: 결제 준비 요청 식별 id -> 다음 백엔드 서버로 요청시 제공<br><br>" +
                          "실패시: redirectUrl 에 .../payment/fail 이 담기고, id 가 null<br>" +
                          "취소시: redirectUrl 에 .../payment/cancel 이 담기고, id 가 null<br> ")
    @Schema(name = "kakaoPay")
    @PostMapping("/ready")
    public PayReadyVo ready(@RequestBody PayInfoDto dto) {
        return kakaoPayService.ready(dto);
    }

    @Operation(summary = "카카오페이 결제 진행",
            description = "모두 필수<br>" +
                          "id: /api/kakao/ready 에서 제공받은 id 제공<br>" +
                          "pg_token: redirectUrl 에 포함되어 있는 '?pg_token=' 의 값을 추출하여 제공<br><br>" +
                          "성공시:<br>" +
                          "totalPrice: 전체 결제금액 (보증금 포함)<br>" +
                          "tax: 과세액 -> 사용하지 않아도 됨<br>" +
                          "itemName: 결제된 상품 이름<br>" +
                          "createdAt: 결제 요청 시점<br>" +
                          "approvedAt: 결제 승인 시점<br>" +
                          "id: 전송했던 id, 백엔드로 결제등록 요청시 함께 전송 (POST: /api/pay)<br>" +
                          "<br>" +
                          "실패시: 예외 발생")
    @Schema(name = "kakaoPay Approve")
    @GetMapping("/success/{id}")
    public PayApproveVo approve(@PathVariable Long id,
                                @RequestParam("pg-token") String pgToken) {
        return kakaoPayService.approve(pgToken, id);
    }

}
