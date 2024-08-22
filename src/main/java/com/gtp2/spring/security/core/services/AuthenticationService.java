package com.gtp2.spring.security.core.services;

import com.gtp2.spring.security.core.dto.LoginDto;
import com.gtp2.spring.security.core.dto.RegisterDto;
import com.gtp2.spring.security.core.models.User;
import com.gtp2.spring.security.core.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,  JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User signup(RegisterDto input) {
        try {
            String firstName = input.getFirstName();
            String lastName = input.getLastName();
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(input.getEmail());
            user.setAddress(input.getAddress());
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            User createdUser = userRepository.save(user);

            return createdUser;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred");
        }
    }


    public User authenticate(LoginDto input) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
            return userRepository.findByEmail(input.getEmail())
                    .orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred");
        }
    }

}
