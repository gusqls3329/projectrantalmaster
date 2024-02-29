package com.team5.projrental.aachat;

import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.model.ResVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;



    @GetMapping("room")
    public List<ChatSelVo> getRoomList(Integer page) {
        return service.getRoomList(page);
    }

    @GetMapping("{ichat}")
    public List<ChatMsgSelVo> getChatList(@PathVariable("ichat") Long ichat, Integer page) {

        return service.getChatList(ichat, (page - 1) * Const.CHAT_MSG_PER_PAGE);
    }

    @PostMapping("room/{target-iuser}")
    public ResVo postRoom(@PathVariable("target-iuser") Long targetIuser, @RequestParam Long iproduct) {
        return service.postRoom(targetIuser, iproduct);
    }

}

