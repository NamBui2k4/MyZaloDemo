package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProfileResponse {
    @Data
    @AllArgsConstructor
    public static class ContactDto {
        private String contactName;
    }
    private String name;
    private String avatarUrl;
    private String email;
    private String phone;
    private Boolean hideOnline;
    private List<ContactDto> contactDtoList;
}
