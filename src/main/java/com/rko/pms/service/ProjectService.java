package com.rko.pms.service;

import com.rko.pms.dto.ProjectDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAllProjectsInRange(LocalDateTime start, LocalDateTime end);
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    void deleteProject(Long id);
}