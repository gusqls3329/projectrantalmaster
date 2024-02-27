package com.team5.projrental.dispute;

import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.dispute.model.DisputeDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/dispute")
public class DisputeController {


    private final DisputeService disputeService;

    @Operation(summary = "채팅 신고",
            description = "신고할 제품 pk 를 identity 로 제공 (제품 자체의 pk 를 제공)<br>" +
                          "    DELETE_BY_ADMIN(0)<br>" +
                          "    <br>분쟁<br>" +
                          "    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1)<br>" +
                          "    CAN_NOT_CONTACT_AFTER_ACTIVATE(2)<br>" +
                          "    FALSE(3)<br>" +
                          "    DIF_PRODUCT(4)<br>" +
                          "    MANNER(5)<br>" +
                          "    LATE(6)<br><br>" +
                          "    // 사고<br>" +
                          "    DAMAGED(-1)<br>" +
                          "    LOSE(-2)<br>")
    @PostMapping("product")
    public ResVo postDisputeProduct(DisputeDto dto) {
        return disputeService.postDisputeProduct(dto);
    }

    @Operation(summary = "채팅 신고",
            description = "신고할 유저 pk 를 identity 로 제공 (유저 자체의 pk 를 제공)<br>" +
                          "    DELETE_BY_ADMIN(0)<br>" +
                          "    <br>분쟁<br>" +
                          "    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1)<br>" +
                          "    CAN_NOT_CONTACT_AFTER_ACTIVATE(2)<br>" +
                          "    FALSE(3)<br>" +
                          "    DIF_PRODUCT(4)<br>" +
                          "    MANNER(5)<br>" +
                          "    LATE(6)<br><br>" +
                          "    // 사고<br>" +
                          "    DAMAGED(-1)<br>" +
                          "    LOSE(-2)<br>")
    @PostMapping("user")
    public ResVo postDisputeUser(DisputeDto dto) {
        return disputeService.postDisputeUser(dto);
    }

    @Operation(summary = "채팅 신고",
            description = "신고할 채팅 pk 를 identity 로 제공 (방 자체의 pk 를 제공)<br>" +
                          "    DELETE_BY_ADMIN(0)<br>" +
                          "    <br>분쟁<br>" +
                          "    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1)<br>" +
                          "    CAN_NOT_CONTACT_AFTER_ACTIVATE(2)<br>" +
                          "    FALSE(3)<br>" +
                          "    DIF_PRODUCT(4)<br>" +
                          "    MANNER(5)<br>" +
                          "    LATE(6)<br><br>" +
                          "    // 사고<br>" +
                          "    DAMAGED(-1)<br>" +
                          "    LOSE(-2)<br>")
    @PostMapping("chat")
    public ResVo postDisputeChat(DisputeDto dto) {
        return disputeService.postDisputeChat(dto);
    }

    @Operation(summary = "채팅 신고",
            description = "신고할 결제 pk 를 identity 로 제공 (결제 자체의 pk 를 제공)<br>" +
                          "    DELETE_BY_ADMIN(0)<br>" +
                          "    <br>분쟁<br>" +
                          "    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1)<br>" +
                          "    CAN_NOT_CONTACT_AFTER_ACTIVATE(2)<br>" +
                          "    FALSE(3)<br>" +
                          "    DIF_PRODUCT(4)<br>" +
                          "    MANNER(5)<br>" +
                          "    LATE(6)<br><br>" +
                          "    // 사고<br>" +
                          "    DAMAGED(-1)<br>" +
                          "    LOSE(-2)<br>")
    @PostMapping("payment")
    public ResVo postDisputePayment(DisputeDto dto) {
        return disputeService.postDisputePayment(dto);
    }


    @Operation(summary = "채팅 신고",
            description = "신고할 자유 게시판 pk 를 identity 로 제공 (자유 게시판 자체의 pk 를 제공)<br>" +
                          "    DELETE_BY_ADMIN(0)<br>" +
                          "    <br>분쟁<br>" +
                          "    CAN_NOT_CONTACT_BEFORE_ACTIVATE(1)<br>" +
                          "    CAN_NOT_CONTACT_AFTER_ACTIVATE(2)<br>" +
                          "    FALSE(3)<br>" +
                          "    DIF_PRODUCT(4)<br>" +
                          "    MANNER(5)<br>" +
                          "    LATE(6)<br><br>" +
                          "    // 사고<br>" +
                          "    DAMAGED(-1)<br>" +
                          "    LOSE(-2)<br>")
    @PostMapping("board")
    public ResVo postDisputeBoard(DisputeDto dto) {
        return disputeService.postDisputeBoard(dto);
    }

}
