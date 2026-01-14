package org.example.service;

import jakarta.validation.constraints.Null;
import org.example.entity.User;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.exception.UserNotFoundException;
@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepo;

    /* === Create === */
    public User createUser(User user){
        if (userRepo.existsByPhone(user.getPhone())){
            throw new RuntimeException("Phone already exist");
        };
        if (userRepo.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exist");
        };
        return userRepo.save(user);
    };

    /* ==== Read ==== */

    public User getUserById(Integer id ){
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    public User updateProfile(Integer userId, String name, String avatarUrl) {
        User user = getUserById(userId);
        user.setName(name);
        user.setAvatarUrl(avatarUrl);
        return userRepo.save(user);
    }

    public User updatePrivacy(Integer userId, boolean hideOnline, boolean hideLastSeen) {
        User user = getUserById(userId);
        user.setHideOnline(hideOnline);
        user.setHideLastSeen(hideLastSeen);
        return userRepo.save(user);
    }
}

