package com.gtp2.spring.security.core.dto;

import com.gtp2.spring.security.core.Enum.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignRoleDto {

    @NotBlank(message = "Role is required")
    @Size(min=4, max = 9)
    private Role role;
}
