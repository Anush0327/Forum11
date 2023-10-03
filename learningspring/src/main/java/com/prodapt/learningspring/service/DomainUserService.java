package com.prodapt.learningspring.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.controller.exception.CustomUserNotFoundException;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.UserRepository;

@Service
public class DomainUserService {

    private static final String ENCODING_STRATEGY = "{bcrypt}";

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    
    public DomainUserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> getByName(String name) {
        return userRepository.findByName(name);
    }

    private String prefixEncodingStrategyAndEncode(String rawString) {
        return ENCODING_STRATEGY + passwordEncoder.encode(rawString);
    }

    public User save(String username, String password,String email,String dob,String token) {
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setToken(token);
        user.setDateOfBirth(dob);
        user.setPassword(prefixEncodingStrategyAndEncode(password));
        return userRepository.save(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByToken(String token){
        return userRepository.findByToken(token);
    }

    public void updateResetPassword(String email,String token) throws CustomUserNotFoundException{
        User user = findUserByEmail(email);

        if(user != null){
            user.setToken(token);
            userRepository.save(user);
        }else{
            throw new CustomUserNotFoundException("No User found with the E-mail ->"+email);
        }
    }

    public void updatePassword(String password,String token){
        User user = findUserByToken(token);
        user.setPassword(prefixEncodingStrategyAndEncode(password));
        user.setToken(null);
        userRepository.save(user);
    }

    public String getCryptPassword(String password){
        return passwordEncoder.encode(password);
    }


}