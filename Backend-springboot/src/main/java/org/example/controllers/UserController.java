package org.example.controllers;

import org.example.dto.response.UserResponseDTO;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.response.ProfileResponse;
import org.example.entity.User;
import org.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ========= CREATE ========= */
    @PostMapping("/create")
    public UserResponseDTO createUser(@RequestBody RegisterRequest dto) {
        User user = userService.createUser(dto);

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .name(user.getName())
                .build();
    }

    /* ========= READ ========= */
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .name(user.getName())
                .build();
    }

    @GetMapping("/all")
    public List<User> getAllUser(){return  userService.getAllUser();};

    @GetMapping("/phone/{phone}")
    public UserResponseDTO getByPhone(@PathVariable String phone) {
        User user = userService.getByPhone(phone);
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .name(user.getName())
                .build();
    }

    /* ========= UPDATE ========= */
    @GetMapping("/{id}/profile")
    public ProfileResponse getProfile(
            @PathVariable Integer id
    ) {
        User user = userService.showProfile(id);
        List<ProfileResponse.ContactDto> contactDtos = user.getContactList().stream()
                .map(contact -> {
                    ProfileResponse.ContactDto dto = new ProfileResponse.ContactDto();
                    dto.setContactName(contact.getContactName());
                    return dto;
                })
                .toList();
        return ProfileResponse.builder()
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .hideOnline(user.getHideOnline())
                .phone(user.getPhone())
                .email(user.getEmail())
                .contactDtoList(contactDtos)
                .build();
    }

    @PutMapping("/{id}/profile")
    public ProfileResponse updateProfile(
            @PathVariable Integer id,
            @RequestBody UpdateProfileRequest request
    ) {
        User user = userService.updateProfile(id,request);
        return ProfileResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    @PutMapping("/{id}/privacy")
    public UserResponseDTO updatePrivacy(
            @PathVariable Integer id,
            @RequestParam boolean hideOnline,
            @RequestParam boolean hideLastSeen
    ) {
        User user = userService.updatePrivacy(id, hideOnline, hideLastSeen);
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .name(user.getName())
                .build();
    }

    /* Delete */

    @PostMapping("/delete")
    public UserResponseDTO deleteAccount(@PathVariable Integer userId){
        User user = userService.deleteAccountById(userId);
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .name(user.getName())
                .build();
    }
}
