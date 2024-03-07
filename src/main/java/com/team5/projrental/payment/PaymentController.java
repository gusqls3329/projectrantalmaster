package com.team5.projrental.payment;

import com.team5.projrental.admin.model.PaymentInfoVo;
import com.team5.projrental.common.exception.ErrorMessage;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.payment.model.PaymentInsDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/pay")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 정보 등록",
            description = "<strong>결제 정보 등록</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v] iproduct: 결제한 제품의 PK<br>" +
                          "[v] rentalStartDate: 실제 계약된 제품 대여 시작일<br>" +
                          "[v] rentalEndDate: 실제 계약된 제품 대여 마감일<br>" +
                          "[v] paymentMethod: 결제 수단 -> credit-card, kakao-pay<br>" +
                          "[v] paymentDetailId: 카카오 페이 결제시 리턴받은 id값" +
                          "<br>" +
                          "성공시: <br>" +
                          "result: 1" +
                          "<br><br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @PostMapping
    public ResVo postPayment(@Validated @RequestBody PaymentInsDto dto) {
        return paymentService.postPayment(dto);
    }

    @Operation(summary = "결제 삭제 or 숨김 or 취소 요청",
            description = "<strong>결제 삭제 또는 숨김 또는 취소 요청</strong><br>" +
                          "[ [v] : 필수값 ]<br>" +
                          "[v] ipayment: 요청할 결제의 PK<br>" +
                          "[v] div: 삭제, 숨김, 취소 요청 식별값<br>" +
                          "     ㄴ> 1: 삭제 요청, 2: 숨김 요청, 3: 취소 요청<br>" +
                          "<br>" +
                          "성공시: <br>" +
                          "result: <br>" +
                          " ㄴ> -1: 삭제됨, -2: 숨겨짐, -3: 취소됨<br>" +
                          "<br>" +
                          "실패시: <br>" +
                          "message: 에러 발생 사유<br>errorCode: 에러 코드")
    @Validated
    @DeleteMapping("/{ipay}")
    public ResVo delPayment(@PathVariable("ipay")
                            @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
                            @Min(value = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
                            Long ipayment,
                            @RequestParam
                            @NotNull(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
                            @Range(min = 1, max = 3, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
                            Integer div) {
        return paymentService.delPayment(ipayment, div);
    }


    @Operation(summary = "특정 결제의 결제정보 조회",
            description = "해당 결제의 상세정보 조회<br>" +
                          "ipayment: 조회한 결제 식별번호 <br>" +
                          "<br>" +
                          "<성공시><br>" +
                          "method: 결제수단<br>" +
                          "totalPrice: 전체 대여금액 (보증금 제외)<br>" +
                          "deposit: 보증금 <br>" +
                          "rentalStartDate: 대여시작일<br>" +
                          "rentalEndDate: 대여 종료일<br>" +
                          "code: 결제 식별 코드<br>" +
                          "myPaymentCode: 상대방이 입력해야할 나의 결제코드 (대면시 입력)<br>" +
                          "ipayment: 결제 식별 번호<br>" +
                          "title: 결제한 제품의 제목<br>" +
                          "prodMainPic: 결제한 제품의 사진<br>" +
                          "paymentStatus: 결제 상태" +
                          "<br><br>" +
                          "<실패시><br>" +
                          "예외 발생")
    @GetMapping("/{ipayment}")
    public PaymentInfoVo getPaymentInfo(@PathVariable
                                        @Min(value = 1, message = ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE)
                                        Long ipayment) {
        return paymentService.getPaymentInfo(ipayment);
    }

    @Operation(summary = "거래 성사 혹은 반납 완료 요청",
            description = "code: 해당 유저가 입력한 상대방이 가지고 있는 결제코드 (성공시 결제코드 변경됨)<br><br>" +
                          "<성공시> <br>" +
                          "result: 1<br>" +
                          "<실패시><br>" +
                          "예외 발생")
    @GetMapping("/code")
    public ResVo patchPayInfo(@RequestParam
                              @NotBlank(message = ErrorMessage.CAN_NOT_BLANK_EX_MESSAGE)
                              String code) {

        return paymentService.patchPayInfo(code);

    }
}
