package com.team5.projrental.chatbot;

import com.team5.projrental.chatbot.model.ChatBotVo;
import com.team5.projrental.chatbot.repository.ChatBotRepository;
import com.team5.projrental.entities.ChatBot;
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

    public List<ChatBotVo> pub(Integer grp, Integer depth, Integer level) {

        // 제공 된것과 같은 grp , depth + 1 인것 다 조회

        return chatBotRepository.findByGrpAndDepth(grp, depth + 1)
                .stream().map(cb -> ChatBotVo.builder()
                        .grp(cb.getGrp())
                        .level(cb.getLevel())
                        .depth(cb.getDepth())
                        .mention(cb.getMention())
                        .build()).collect(Collectors.toList());
    }
}
