package com.team5.projrental.aachat.configuration;

import com.team5.projrental.aachat.handler.StompPreHandler;
import com.team5.projrental.aachat.properties.RabbitMQProperties;
import com.team5.projrental.aachat.stomp.StompErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {
    private final StompPreHandler stompPreHandler;
    private final StompErrorHandler stompErrorHandler;
    private final RabbitMQProperties properties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(stompErrorHandler)
                .addEndpoint(properties.getStompEndPoint())
                .setAllowedOriginPatterns(properties.getStompAllowedOriginPatterns());
                //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setPathMatcher(new AntPathMatcher("."))
                .setApplicationDestinationPrefixes(properties.getStompApplicationDestinationPrefixes())
                .enableStompBrokerRelay(properties.getStompBrokerRelay())
                .setRelayHost(properties.getHost())
                .setRelayPort(properties.getStompRelayPort())
                .setVirtualHost(properties.getVirtualHost())
                .setClientLogin(properties.getUsername())
                .setClientPasscode(properties.getPassword())
                .setSystemLogin(properties.getUsername())
                .setSystemPasscode(properties.getPassword());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompPreHandler);
    }

}
