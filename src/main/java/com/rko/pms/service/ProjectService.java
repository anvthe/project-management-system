package com.rko.pms.service;

import com.rko.pms.domain.Project;
import com.rko.pms.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByDateRange(LocalDateTime start, LocalDateTime end) {
        return projectRepository.findAllByStartDateTimeBetween(start, end);
    }

    public Project updateProject(Long id, Project project) {
        Project pro = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(pro.getName());
        project.setIntro(pro.getIntro());
        project.setOwner(pro.getOwner());
        project.setStatus(pro.getStatus());
        project.setStartDateTime(pro.getStartDateTime());
        project.setEndDateTime(pro.getEndDateTime());
        project.setProjectMembers(pro.getProjectMembers());
        return projectRepository.save(pro);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

}
