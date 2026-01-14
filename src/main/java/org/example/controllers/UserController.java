package org.example.controllers;

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
    @PutMapping("/{id}/profile")
    public User updateProfile(
            @PathVariable Integer id,
            @RequestParam String name,
            @RequestParam(required = false) String avatarUrl
    ) {
        return userService.updateProfile(id, name, avatarUrl);
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
