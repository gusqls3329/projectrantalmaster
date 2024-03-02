package com.team5.projrental.dispute;

import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.dispute.model.DisputeDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/dispute")
public class DisputeController {


    private final DisputeService disputeService;

    @Operation(summary = "상품 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "reason : 신고사유(숫자, 노션에 번호있음)<br>" +
                          "identity : 상품 = 상품번호(pk)<br>" +
                          "details : 신고 사유(글) - \"\" 도 가능.<br>")

    @PostMapping("product")
    public ResVo postDisputeProduct(@Validated DisputeDto dto) {
        return disputeService.postDisputeProduct(dto);
    }

    @Operation(summary = "유저 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "reason : 신고사유(숫자, 노션에 번호있음)<br>" +
                          "identity : 유저 = 유저번호 pk<br>" +
                          "details : 신고 사유(글) - \"\" 도 가능.<br>")
    @PostMapping("user")
    public ResVo postDisputeUser(@Validated DisputeDto dto) {
        return disputeService.postDisputeUser(dto);
    }

    @Operation(summary = "채팅 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "reason : 신고사유(숫자, 노션에 번호있음)<br>" +
                          "identity : 채팅 = 채팅pk<br>" +
                          "details : 신고 사유(글) - \"\" 도 가능.<br>")
    @PostMapping("chat")
    public ResVo postDisputeChat(@Validated DisputeDto dto) {
        return disputeService.postDisputeChat(dto);
    }

    @Operation(summary = "결제 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "reason : 신고사유(숫자, 노션에 번호있음)<br>" +
                          "identity : 결제 = 결제번호(pk)<br>" +
                          "details : 신고 사유(글) - \"\" 도 가능.<br>")
    @PostMapping("payment")
    public ResVo postDisputePayment(@Validated DisputeDto dto) {
        return disputeService.postDisputePayment(dto);
    }


    @Operation(summary = "자유게시판 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "reason : 신고사유(숫자, 노션에 번호있음)<br>" +
                          "identity : 자유게시판 = 자유게시판번호(pk) or 유저 = 유저pk ...등<br>" +
                          "details : 신고 사유(글) - \"\" 도 가능.<br>")
    @PostMapping("board")
    public ResVo postDisputeBoard(@Validated DisputeDto dto) {
        return disputeService.postDisputeBoard(dto);
    }

}
