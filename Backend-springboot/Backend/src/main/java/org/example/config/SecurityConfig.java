package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Tắt CSRF (API, Postman, Swagger)
                .csrf(csrf -> csrf.disable())

                // ❌ Tắt Basic Auth (nguyên nhân Swagger hỏi username/password)
                .httpBasic(httpBasic -> httpBasic.disable())

                // ❌ Tắt form login
                .formLogin(form -> form.disable())

                // ✅ Cho phép truy cập tự do
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",           // Cho phép tất cả dưới /swagger-ui/
                                "/v3/api-docs/**",          // OpenAPI docs
                                "/auth/**"                  // endpoint auth của bạn
                        ).permitAll()
                        .anyRequest().permitAll()       // hoặc .authenticated() nếu muốn bảo vệ
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
