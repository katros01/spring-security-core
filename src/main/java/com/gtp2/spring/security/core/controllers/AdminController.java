package com.gtp2.spring.security.core.controllers;

import com.gtp2.spring.security.core.Enum.Role;
import com.gtp2.spring.security.core.dto.AssignRoleDto;
import com.gtp2.spring.security.core.models.User;
import com.gtp2.spring.security.core.services.UserService;
import com.gtp2.spring.security.core.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/assign-role/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CustomResponse<User>> assignRoleToUser(
            @PathVariable String id,
            @RequestBody AssignRoleDto assignRoleBody) {
        try {
            System.out.println("here");
            User user = userService.assignRoleToUser(id, assignRoleBody.getRole());
            return new ResponseEntity<>(new CustomResponse<>("Role " + assignRoleBody.getRole() + " assigned to user " + user.getEmail(), HttpStatus.OK.value(), user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse<>((e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CustomResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(new CustomResponse<>("Products retrieved successfully", HttpStatus.OK.value(), users), HttpStatus.OK);
    }

}
