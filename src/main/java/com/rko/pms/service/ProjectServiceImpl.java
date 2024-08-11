package com.rko.pms.service;

import com.rko.pms.UserUtil;
import com.rko.pms.domain.User;
import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;
import com.rko.pms.repository.ProjectRepository;
import com.rko.pms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public void validateProjectOwnership(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        String currentUsername = userUtil.getUserName();
        Long currentUserId = userUtil.getUserIdByUsername(currentUsername);

        if (!project.getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("You are not the owner of this project.");
        }
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectDTO.getProjectMemberUsernames() != null && projectDTO.getProjectMemberUsernames().size() > 5) {
            throw new IllegalArgumentException("A project cannot have more than 5 members.");
        }

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setIntro(projectDTO.getIntro());
        project.setStatus(projectDTO.getStatus());
        project.setStartDateTime(projectDTO.getStartDateTime());
        project.setEndDateTime(projectDTO.getEndDateTime());

        User owner = userRepository.findById(projectDTO.getOwnerId()).orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        project.setOwner(owner);

        if (projectDTO.getProjectMemberUsernames() != null && !projectDTO.getProjectMemberUsernames().isEmpty()) {
            Set<User> projectMembers = projectDTO.getProjectMemberUsernames().stream().map(username -> userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username))).collect(Collectors.toSet());
            project.setProjectMembers(projectMembers);
        }

        Project savedProject = projectRepository.save(project);

        Set<String> usernames = savedProject.getProjectMembers().stream().map(User::getUsername).collect(Collectors.toSet());

        return new ProjectDTO(savedProject.getId(), savedProject.getName(), savedProject.getIntro(), savedProject.getOwner().getId(), savedProject.getStatus(), savedProject.getStartDateTime(), savedProject.getEndDateTime(), usernames);
    }

    public List<ProjectDTO> findAllProjectsInRange(LocalDateTime start, LocalDateTime end) {
        List<Project> projects = projectRepository.findAllByStartDateTimeBetween(start, end);
        return projects.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ProjectDTO convertToDto(Project project) {
        return getProjectDTO(project);
    }

    private ProjectDTO getProjectDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setIntro(project.getIntro());
        dto.setOwnerId(project.getOwner().getId());
        dto.setStatus(project.getStatus());
        dto.setStartDateTime(project.getStartDateTime());
        dto.setEndDateTime(project.getEndDateTime());
        dto.setProjectMemberUsernames(project.getProjectMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        return dto;
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        validateProjectOwnership(id);

        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        existingProject.setName(projectDTO.getName());
        existingProject.setIntro(projectDTO.getIntro());
        existingProject.setStatus(projectDTO.getStatus());
        existingProject.setStartDateTime(projectDTO.getStartDateTime());
        existingProject.setEndDateTime(projectDTO.getEndDateTime());
        Project updateProject = projectRepository.save(existingProject);

        return convetToProjectDTO(updateProject);

    }

    private ProjectDTO convetToProjectDTO(Project project) {
        return getProjectDTO(project);
    }

    public void deleteProject(Long id) {
        validateProjectOwnership(id);
        projectRepository.deleteById(id);
    }
}