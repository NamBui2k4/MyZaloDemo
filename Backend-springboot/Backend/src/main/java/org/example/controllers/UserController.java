package org.example.controllers;

import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.response.UserProfileResponse;
import org.example.entity.User;
import org.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ========= CREATE ========= */
    @PostMapping()
    public User createUser(@RequestBody User user) {
        System.out.println("Received user: " + user);  // Log để xem body có map được không
        return userService.createUser(user);
    }

    /* ========= READ ========= */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/phone/{phone}")
    public User getByPhone(@PathVariable String phone) {
        return userService.getByPhone(phone);
    }

    /* ========= UPDATE ========= */
    @GetMapping("/{id}/profile")
    public UserProfileResponse getProfile(
            @PathVariable Integer id
    ) {
        return userService.showProfile(id);
    }

    @PutMapping("/{id}/profile")
    public UserProfileResponse updateProfile(
            @PathVariable Integer id,
            @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateProfile(id,request);
    }

    @PutMapping("/{id}/privacy")
    public User updatePrivacy(
            @PathVariable Integer id,
            @RequestParam boolean hideOnline,
            @RequestParam boolean hideLastSeen
    ) {
        return userService.updatePrivacy(id, hideOnline, hideLastSeen);
    }
}
