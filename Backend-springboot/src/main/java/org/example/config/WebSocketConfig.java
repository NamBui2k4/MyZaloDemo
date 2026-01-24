package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

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
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(
                        new DefaultHandshakeHandler(){
                            @Override
                            protected Principal determineUser(ServerHttpRequest request,
                                                              WebSocketHandler webSocketHandler,
                                                              Map<String, Object> attributes){
                                String uri = request.getURI().toString();
                                String userId = "unknown user";
                                if (uri.contains("userId=")){
                                    userId = uri.split("userId=")[1];
                                }
                                return new StompPrincipal(userId);
                            }
                        }
                );
    }
}
