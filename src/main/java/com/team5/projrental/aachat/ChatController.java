package com.team5.projrental.aachat;

import com.team5.projrental.aachat.model.ChatMsgSelVo;
import com.team5.projrental.aachat.model.ChatSelVo;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.model.ResVo;
import io.swagger.v3.oas.annotations.Operation;
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

    //로그인 유저의 모든 채팅방 카운트
    @GetMapping("count")
    @Operation(summary = "채팅방 수", description = "로그인 유저가 참여중인 채팅방 수")
    public ResVo getChatCount() {
        return service.getChatCount();
    }

    //로그인 유저의 채팅 리스트
    @GetMapping()
    @Operation(summary = "채팅방 리스트", description = "로그인 유저가 참여중인 채팅방 리스트")
    public List<ChatSelVo> getChatAll(Integer page) {
        return service.getRoomList(page);
    }

    @GetMapping("{ichat}")
    @Operation(summary = "채팅방 메세지 리스트", description = "로그인 유저가 입장한 채팅방의 메세지 리스트")
    public List<ChatMsgSelVo> getChatList(@PathVariable("ichat") Long ichat, Integer page) {

        return service.getChatList(ichat, (page - 1) * Const.CHAT_MSG_PER_PAGE);
    }

    @PostMapping("room/{target-iuser}")
    public ResVo postRoom(@PathVariable("target-iuser") Long targetIuser, @RequestParam Long iproduct) {
        return service.postRoom(targetIuser, iproduct);
    }


}

