package com.rko.pms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String name;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters & Username must not be blank")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[!@#$%^&*]).{5,}$", message = "Password must be at least 5 characters long and contain at least one special character")
    private String password;
}