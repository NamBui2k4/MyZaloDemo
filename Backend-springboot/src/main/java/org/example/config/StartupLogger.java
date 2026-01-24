package org.example.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupLogger {

    @Value("${server.port:8080}")
    private String port;

    @Value("${spring.application.name:chat-backend}")
    private String appName;

    @EventListener(ApplicationReadyEvent.class)
    public void logStartup() {
        log.info("==============================================");
        log.info("🚀 Application '{}' started successfully", appName);
        log.info("🌐 Base URL      : http://localhost:{}", port);
        log.info("📘 Swagger (if any): http://localhost:{}/swagger-ui.html", port);
        log.info("==============================================");
    }
}
