package com.team5.projrental.common.sse.holder;

import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.sse.enums.SseCode;
import com.team5.projrental.common.sse.enums.SseKind;
import com.team5.projrental.common.sse.model.SseMessageInfo;
import com.team5.projrental.common.sse.responseproperties.Code;
import com.team5.projrental.common.sse.responseproperties.Message;
import com.team5.projrental.common.sse.third.PushRepository;
import com.team5.projrental.common.threadpool.MyThreadPoolHolder;
import com.team5.projrental.entities.Push;
import com.team5.projrental.entities.inheritance.Users;
import com.team5.projrental.user.UserRepository;
import com.team5.projrental.user.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class SseEmitterHolder {
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();


    private final MyThreadPoolHolder myThreadPoolHolder;
    private final UsersRepository usersRepository;
    private final PushRepository pushRepository;


    /**
     * code 에 구분자로 필요한 성질을 동적으로 추가함.
     *
     * @param iuser
     * @return SseEmitter
     */
    public SseEmitter add(Long iuser) {
        SseEmitter sseEmitter = new SseEmitter(Const.SSE_TIMEOUT_TIME);
        emitterMap.put(iuser, sseEmitter);

        // SseEmitter 는 콜백을 필드에 저장함.
        sseEmitter.onCompletion(() -> emitterMap.remove(iuser));
        sseEmitter.onTimeout(() -> emitterMap.remove(iuser));

        try {
            sseEmitter.send("connect success");
        } catch (IOException e) {
            log.info("[SseEmitterHolder.add] Exception", e);
            throw new RuntimeException(e);
        }


        myThreadPoolHolder.getThreadPool().execute(() -> sendFromDbMessage(iuser));

        return sseEmitter;
    }

    // + 만약 해당 유저의 SseEmitter 가 존재하지 않으면 DB 에 저장해두고, 로그인시 해당 데이터 일괄 보내기
    // 여기서 필요한건 DB에서 해당 유저에게 보내야할 푸시가 존재한다면 해당 메시지 다 담아서 푸시하기.
    public void sendFromDbMessage(Long iuser) {
        // select iusers, description, code, identity_num, kind from push_message where iuser = #{iuser}
        // select * from push_message where iusers = #{iuser}

        Users users = usersRepository.findById(iuser).get();
        List<Push> findPush = pushRepository.findByUsers(users);

        for (Push push : findPush) {
            send(SseMessageInfo.builder()
                    .receiver(push.getUsers().getId())
                    .description(push.getDescription())
                    .code(push.getCode().getNum())
                    .identityNum(push.getIdentityNum())
                    .kind(push.getKind().getNum())
                    .build());
        }
        long result = pushRepository.deleteUseIn(findPush);
        log.info("result = {}", result);
    }


    // + 만약 해당 유저의 SseEmitter 가 존재하지 않으면 DB 에 저장해두고, 로그인시 해당 데이터 일괄 보내기
    // 여기서 필요한건 SseEmitter 가 존재하지 않으면 DB 에 저장.
    public boolean send(SseMessageInfo info) {
        return send(info, "sse push");
    }

    public boolean send(SseMessageInfo info, String eventName) {
        try {
            emitterMap.get(info.getReceiver()).send(SseEmitter.event()
                    .name(eventName)
                    .data(info, MediaType.APPLICATION_JSON)
            );

        } catch (NullPointerException e) {
            Push savePush = Push.builder()
                    .users(usersRepository.findById(info.getReceiver()).orElseThrow(() -> new ClientException(ErrorCode.NO_SUCH_USER_EX_MESSAGE,
                            "잘못된 유저정보입니다 (ireceiver)")))
                    .description(info.getDescription())
                    .code(SseCode.getByNum(info.getCode()))
                    .identityNum(info.getIdentityNum())
                    .kind(SseKind.getByNum(info.getKind()))
                    .build();
            pushRepository.save(savePush);

            return false;
        } catch (IOException e) {
            log.info("[SseEmitterHolder.send] Exception", e);
            return false;
        }
        return true;
    }

}
