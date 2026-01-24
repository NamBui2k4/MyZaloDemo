package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.User;

@Getter
@Setter
public class UserUpdateProfileResponse extends  UserProfileResponse{
    private String email;
    private String phone;

    public UserUpdateProfileResponse(User u){
        super(u.getAvatarUrl(), u.getName());

        this.email = u.getEmail();
        this.phone = u.getPhone();
    }
}
