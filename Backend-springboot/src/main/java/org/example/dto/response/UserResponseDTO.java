package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Integer userId;
    private String phone;
    private String email;
    private String name;
    private String avatarUrl;
}
