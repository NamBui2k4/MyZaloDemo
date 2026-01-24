package org.example.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.info("➡️ {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("❌ Request failed: {}", e.getMessage());
            throw e;
        } finally {
            String status = String.valueOf(response.getStatus());

            if (status.matches("4\\d{2}")){
                log.info("❌ Status: {}", status);
            }else if(status.matches("5\\d{2}")){
                log.info("\uD83D\uDD25 Server Error Status: {}", status);
            } else if (status.matches("2\\d{2}")) {
                log.info("✅ Success Status: {}", status);
            } else {
                log.info("ℹ️ Other Status: {}", status);
            }
        }
    }

}
