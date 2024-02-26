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

    @PostMapping("product")
    public ResVo postDisputeProduct(DisputeDto dto) {
        return disputeService.postDisputeProduct(dto);
    }

    @PostMapping("user")
    public ResVo postDisputeUser(DisputeDto dto) {
        return disputeService.postDisputeUser(dto);
    }

    @Operation(summary = "채팅 신고",
            description = "신고할 채팅 pk 를 identity 로 제공 (방 자체의 pk 를 제공)")
    @PostMapping("chat")
    public ResVo postDisputeChat(DisputeDto dto) {
        return disputeService.postDisputeChat(dto);
    }

    @PostMapping("payment")
    public ResVo postDisputePayment(DisputeDto dto) {
        return disputeService.postDisputePayment(dto);
    }

    @PostMapping("board")
    public ResVo postDisputeBoard(DisputeDto dto) {
        return disputeService.postDisputeBoard(dto);
    }

}
