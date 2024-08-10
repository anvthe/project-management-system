package com.rko.pms.dto;

import com.rko.pms.domain.User;
import com.rko.pms.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String intro;
    private Long ownerId;
    private ProjectStatus status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Set<String> projectMemberUsernames;
}