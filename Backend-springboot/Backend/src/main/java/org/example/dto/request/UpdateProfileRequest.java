package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateProfileRequest {
    private String name;
    private String avatarUrl;
    private String phone;
    private String email;
}
