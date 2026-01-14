package org.example.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Đây là chuỗi Secret Key mẫu (đã mã hóa Hex/Base64 đủ 256-bit cho thuật toán HS256)
    // Trong thực tế, bạn nên lưu cái này trong file application.properties
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 1 ngày (24 giờ)

    // 1. SỬA: Thêm tham số userId để khớp với AuthService
    public String generateToken(Integer userId, String username) {
        return Jwts.builder()
                .setSubject(username)           // Lưu số điện thoại/username vào Subject
                .claim("userId", userId)        // SỬA: Lưu thêm userId vào Claim riêng
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // SỬA: Dùng hằng số EXPIRATION_MS
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. SỬA: Lấy userId từ Claim "userId" (không lấy từ subject nữa)
    public Integer extractUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Integer.class);
    }

    // Lấy username (số điện thoại) từ token
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token hết hạn, chữ ký sai, hoặc lỗi cấu trúc
            return false;
        }
    }

    private Key getSigningKey() {
        // SỬA: Dùng biến SECRET đã khai báo ở trên
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims parseToken(String token) {
        return Jwts
                .parserBuilder()                // Chuẩn cho bản 0.11.x
                .setSigningKey(getSigningKey()) // Chuẩn cho bản 0.11.x
                .build()
                .parseClaimsJws(token)          // Chuẩn cho bản 0.11.x
                .getBody();
    }
}