package com.team5.projrental.aachat;

import com.team5.projrental.aachat.model.ChatMsgInsDto;
import com.team5.projrental.aachat.properties.RabbitMQProperties;
import com.team5.projrental.common.SecurityProperties;
import com.team5.projrental.common.security.JwtTokenProvider;
import com.team5.projrental.common.security.SecurityUserDetails;
import com.team5.projrental.common.security.model.SecurityPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final RabbitTemplate template;
    private final RabbitMQProperties properties;
    private final ChatService service;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityProperties appProperties;


    @MessageMapping("chat.send.{ichatRoom}")
    @Operation(summary = "채팅 메세지 발송", description = "채팅 메세지 발송")
    public void send(ChatMsgInsDto dto, @DestinationVariable Long ichatRoom) {

        dto.setIchat(ichatRoom);
        Long otherPersonIuser = service.changeUserStatus(dto.getIchat(), dto.getSenderIuser());

        //service.setSeq(dto);
        // chat.exchange                // room.{ichatRoom}                       // 메시지
        template.convertAndSend(properties.getChatExchangeName(), String.format("%s.%d", "room", ichatRoom), dto);
        service.saveMsg(dto);
    }

//    @RabbitListener(queues = "${spring.rabbitmq.chat-queue-name}")
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "${spring.rabbitmq.chat-exchange-name}", type = ExchangeTypes.TOPIC),
            value = @Queue(name = "${spring.rabbitmq.chat-queue-name}"),
            key = "room.*"
    ))
    public void receive(ChatMsgInsDto dto) {
        System.out.println("received : " + dto.getMessage());
    }

}
