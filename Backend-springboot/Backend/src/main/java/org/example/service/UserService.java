package org.example.service;

import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.response.UserProfileResponse;
import org.example.dto.response.UserUpdateProfileResponse;
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
            throw new UserNotFoundException("Phone already exist");
        };
        if (userRepo.existsByEmail(user.getEmail())){
            throw new UserNotFoundException("Email already exist");
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
    public UserProfileResponse showProfile(Integer userId) {
        User user;
        try{
            user = getUserById(userId);
            return new UserProfileResponse(
                    user.getAvatarUrl(),
                    user.getName()
            );
        }catch (Exception e){
            throw e;
        }

    }

    public UserProfileResponse updateProfile(Integer userId, UpdateProfileRequest req) {
        User user;
        try{
            user = getUserById(userId);

            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setAvatarUrl(req.getAvatarUrl());
            user.setPhone(req.getPhone());

            userRepo.save(user);

            return new UserUpdateProfileResponse(user);
//            return
        }catch (Exception e){
            throw e;
        }

    }

    public User updatePrivacy(Integer userId, boolean hideOnline, boolean hideLastSeen) {
        User user;
        try{
            user = getUserById(userId);
            user.setHideOnline(hideOnline);
            user.setHideLastSeen(hideLastSeen);
            return userRepo.save(user);
        }catch (Exception e){
            throw e;
        }

    }
}

