package com.gtp2.spring.security.core.controllers;


import com.gtp2.spring.security.core.dto.LoginDto;
import com.gtp2.spring.security.core.dto.RegisterDto;
import com.gtp2.spring.security.core.models.User;
import com.gtp2.spring.security.core.services.AuthenticationService;
import com.gtp2.spring.security.core.services.JwtService;
import com.gtp2.spring.security.core.services.UserService;
import com.gtp2.spring.security.core.utils.AuthResponse;
import com.gtp2.spring.security.core.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {


    private AuthenticationService authService;
    private final JwtService jwtService;
    private UserService userService;

    @Autowired
    public AuthController(AuthenticationService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<?>> register(@Valid @RequestBody RegisterDto registerUserBody, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }

                CustomResponse<Map<String, String>> response = new CustomResponse<>(
                        "Validation failed",
                        HttpStatus.BAD_REQUEST.value(),
                        errors
                );

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (userService.emailExists(registerUserBody.getEmail())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "Email already exists");

                CustomResponse<Map<String, String>> response = new CustomResponse<>(
                        "Validation failed",
                        HttpStatus.BAD_REQUEST.value(),
                        errors
                );

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            User registeredUser = authService.signup(registerUserBody);

            return new ResponseEntity<>(new CustomResponse<>("User Registered successfully", HttpStatus.OK.value(), registeredUser), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> authenticate(@Valid @RequestBody LoginDto loginUserDto, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }

                CustomResponse<Map<String, String>> response = new CustomResponse<>(
                        "Validation failed",
                        HttpStatus.BAD_REQUEST.value(),
                        errors
                );

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            User authenticatedUser = authService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            AuthResponse loginResponse = new AuthResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            loginResponse.setRole(authenticatedUser.getRole());
            loginResponse.setFirstName(authenticatedUser.getFirstName());
            loginResponse.setLastName(authenticatedUser.getLastName());
            loginResponse.setEmail(authenticatedUser.getEmail());

            return new ResponseEntity<>(new CustomResponse<>("User logged in", HttpStatus.OK.value(), loginResponse), HttpStatus.OK);

        }catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
