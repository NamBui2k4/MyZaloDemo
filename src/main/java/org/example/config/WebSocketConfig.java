package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // Client subscribe
        config.enableSimpleBroker("/topic", "/queue");

        // Client gửi message tới server
        config.setApplicationDestinationPrefixes("/app");

        // Cho phép /user/{id}/queue/**
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // <--- QUAN TRỌNG: Cho phép Postman kết nối
//                .withSockJS(); // Nếu bạn dùng cái này, xem lưu ý bên dưới
    }
}
