package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
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
    private final WebSocketAuthInterceptor interceptor;

    public WebSocketConfig(WebSocketAuthInterceptor interceptor){
        this.interceptor = interceptor;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/queue")  // relay đến RabbitMQ
                .setRelayHost("rabbitmq")
                .setRelayPort(61613)  // STOMP port của RabbitMQ plugin
                .setClientLogin("guest")
                .setClientPasscode("guest")
                .setSystemLogin("guest")
                .setSystemPasscode("guest");
        registry.setApplicationDestinationPrefixes("/app");
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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptor);
    }

}
