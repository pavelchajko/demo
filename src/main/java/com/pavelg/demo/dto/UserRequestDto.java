package com.pavelg.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank
    private String fullName;
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String password;
}
