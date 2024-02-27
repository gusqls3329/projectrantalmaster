package com.team5.projrental.chatbot;

import com.team5.projrental.chatbot.model.ChatBotVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/bot")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @Operation(summary = "챗봇 첫 대화 응답",
            description = "챗봇 방 입장시 요청, 초기화 채팅 메시지 출력 <br>" +
                          "<br>" +
                          "grp & level & depth 는 차후 챗봇 대화를 지속하기위해 보내주어야 하는 값 (선택된 멘션의 값을)")
    @GetMapping("connect")
    public List<ChatBotVo> chatBotConnect() {
        return chatBotService.chatBotConnect();
    }

    @GetMapping("pub")
    public List<ChatBotVo> pub(@RequestParam("r") Integer grp,
                               @RequestParam("d") Integer depth,
                               @RequestParam("l") Integer level) {

        if (depth == 3) return chatBotConnect();


        return chatBotService.pub(grp, level, depth);
    }

    // 메시지 직접 전송시

}
