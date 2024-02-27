package com.team5.projrental.initializr;

import com.team5.projrental.initializr.model.ChatBotVo;
import com.team5.projrental.initializr.repository.ChatBotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final ChatBotRepository chatBotRepository;


    public List<ChatBotVo> chatBotConnect() {

        return chatBotRepository.findByDepthInit().stream().map(
                cb -> ChatBotVo.builder()
                        .grp(cb.getGrp())
                        .level(cb.getLevel())
                        .depth(cb.getDepth())
                        .mention(cb.getMention())
                        .build()).collect(Collectors.toList());

    }

    public List<ChatBotVo> pub(Integer grp, Integer level, Integer depth) {

        // 선택 된것과 같은 grp ,


        return null;
    }
}
