package com.rko.pms.service;

import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    List<Project> findAllProjectsInRange(LocalDateTime start, LocalDateTime end);
    ProjectDTO createProject(ProjectDTO project);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
}