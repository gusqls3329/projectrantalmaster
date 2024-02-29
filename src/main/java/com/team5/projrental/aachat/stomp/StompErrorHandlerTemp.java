package com.team5.projrental.aachat.stomp;

import com.team5.projrental.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class StompErrorHandlerTemp extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError (Message<byte[]> clientMessage, Throwable ex){
        if (ex.getCause().getMessage().equals("JWT")){
            return handleJwtException(clientMessage, ex);
        }

        if (ex.getCause().getMessage().equals("Auth")){
            return handleUnauthorizedException(clientMessage, ex);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }


    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex) {

        return null;
    }



    private Message<byte[]> handleJwtException(Message<byte[]> clientMessage, Throwable ex) {

    return null;
    }



    private Message<byte[]> prepareErrorMessage(ErrorCode responseCode){

    String code = String.valueOf(responseCode.getMessage());
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
