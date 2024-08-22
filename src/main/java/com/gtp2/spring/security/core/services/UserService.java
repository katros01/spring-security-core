package com.gtp2.spring.security.core.services;

import com.gtp2.spring.security.core.Enum.Role;
import com.gtp2.spring.security.core.models.User;
import com.gtp2.spring.security.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User findByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("user not found"));;
        return user;
    }

    public User assignRoleToUser(String id, Role role) {
        System.out.println(role);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(role);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
