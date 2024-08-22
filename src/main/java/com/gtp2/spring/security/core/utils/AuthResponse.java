package com.gtp2.spring.security.core.utils;

import com.gtp2.spring.security.core.Enum.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;

    private long expiresIn;

    private Role role;

    private String firstName;

    private String lastName;

    private String email;
}
