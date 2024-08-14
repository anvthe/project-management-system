package com.rko.pms.service;

import com.rko.pms.dto.ProjectDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAllProjectsInRange(LocalDate start, LocalDate end);
    ProjectDTO createProject(ProjectDTO projectDTO, String username);
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    void deleteProject(Long id);
    ProjectDTO findProjectById(Long id);
}