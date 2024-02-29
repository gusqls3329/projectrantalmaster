package com.team5.projrental.aachat.handler;

import com.team5.projrental.common.SecurityProperties;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.team5.projrental.common.exception.ErrorCode.BAD_AUTHORIZATION_EX_MESSAGE;


@Slf4j
@RequiredArgsConstructor
@Component
public class StompPreHandler implements ChannelInterceptor {
    private final SecurityProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())){

            String authorizationHeader = accessor.getNativeHeader(appProperties.getJwt().getHeaderSchemeName()) == null ? null : String.valueOf(accessor.getNativeHeader(appProperties.getJwt().getHeaderSchemeName()).get(0));
            if (authorizationHeader == null || authorizationHeader.equals("null")) {
                throw new ClientException(BAD_AUTHORIZATION_EX_MESSAGE, "만료된 토큰 입니다.");
            }
            String token = authorizationHeader.substring(appProperties.getJwt().getTokenType().length() + 1);
            if (token == null || !jwtTokenProvider.isValidatedToken(token)) {
                throw new ClientException(BAD_AUTHORIZATION_EX_MESSAGE, "토큰이 존재하지 않습니다.");
            }
            log.info("command = {}", accessor.getCommand());
            log.info("token = {}", token);
        }
        return message;
    }
}
