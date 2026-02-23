package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.UpdateProfileRequest;
import org.example.dto.response.ProfileResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.example.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    /* === Create === */
    @Transactional
    public User createUser(RegisterRequest dto){

        if (userRepo.existsByPhone(dto.getPhone())){
            throw new UserNotFoundException("Phone already exist");
        };
        if (userRepo.existsByEmail(dto.getEmail())){
            throw new UserNotFoundException("Email already exist");
        };

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(hashedPassword)
                .hideLastSeen(false)
                .hideOnline(false)
                .avatarUrl("")
                .build();
        return userRepo.save(user);
    };

    /* ==== Read ==== */

    public User getUserById(Integer id ){
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

    };
    public List<User> getAllUser(){
        return userRepo.findAll();
    };
    public User getUserByEmailAndPhone(
            String email,
            String phone
    ){
        return userRepo.findByEmail_Phone(email, phone)
                .orElseThrow(()-> new UserNotFoundException("user not found"));
    }
    public User getByPhone(String phone) {
        return userRepo.findByPhone(phone)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    public User showProfile(Integer userId) {
        return userRepo.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("User not found"));
    }

    /* ==== Update ==== */
    @Transactional
    public User updateProfile(Integer userId, UpdateProfileRequest req) {
        User user;
        try{
            user = userRepo.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("user not found"));

            user.setName(req.getName());
            user.setEmail(req.getEmail());
            user.setAvatarUrl(req.getAvatarUrl());
            user.setPhone(req.getPhone());

            return userRepo.save(user);

        }catch (Exception e){
            throw e;
        }

    }

    @Transactional
    public User updatePrivacy(Integer userId, boolean hideOnline, boolean hideLastSeen) {
        User user;
        try{
            user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("user not found"));
            user.setHideOnline(hideOnline);
            user.setHideLastSeen(hideLastSeen);
            return userRepo.save(user);
        }catch (Exception e){
            throw e;
        }

    }

    @Transactional
    public User deleteAccountById(Integer userId){
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("user not found"));
        userRepo.delete(user);
        return  user;
    }

//    @Transactional
//    public User deleteAccountByPhoneAndEmail(String email, String phone, String password){
//        User user = getUserByEmailAndPhone(email, phone);
//
//        String passwordHashed = passwordEncoder.encode(password);
//
//        if(passwordHashed.equals())
//    }

}

