package com.taskai_backend.identity.dto.request;

// SignupRequest.java


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String fullName;
}

