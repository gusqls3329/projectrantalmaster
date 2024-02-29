package com.team5.projrental.aachat.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;
    private String chatQueueName;
    private String chatExchangeName;
    private String chatRoutingKey;
    private int stompRelayPort;
    private String[] stompApplicationDestinationPrefixes;
    private String[] stompEndPoint;
    private String[] stompAllowedOriginPatterns;
    private String[] stompBrokerRelay;

}