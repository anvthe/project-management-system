package com.rko.pms.dto;

import com.rko.pms.domain.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    @Size(max = 1000, message = "Intro must not exceed 1000 characters")
    private String intro;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    @NotNull(message = "Start date and time are required")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time are required")
    private LocalDateTime endDateTime;

    @Size(max = 5)
    private Set<String> projectMemberUsernames;

    private String owner;
}