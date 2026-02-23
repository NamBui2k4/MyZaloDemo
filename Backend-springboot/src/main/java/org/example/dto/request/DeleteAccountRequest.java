package org.example.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class DeleteAccountRequest {

    @Data  public static class AccountVerify{
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String phone;
    }

    @Data public static class PasswordRequest {
        @NotBlank
        private String password;
    }


}
