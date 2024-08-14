package com.rko.pms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, message = "Username length must be four character")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;
}