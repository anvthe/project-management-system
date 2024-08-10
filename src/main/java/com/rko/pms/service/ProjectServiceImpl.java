package com.rko.pms.service;

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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

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

        User owner = userRepository.findById(projectDTO.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        project.setOwner(owner);

        if (projectDTO.getProjectMemberUsernames() != null && !projectDTO.getProjectMemberUsernames().isEmpty()) {
            Set<User> projectMembers = projectDTO.getProjectMemberUsernames().stream()
                    .map(username -> userRepository.findByUsername(username)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username)))
                    .collect(Collectors.toSet());
            project.setProjectMembers(projectMembers);
        }

        Project savedProject = projectRepository.save(project);

        Set<String> usernames = savedProject.getProjectMembers().stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());

        return new ProjectDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getIntro(),
                savedProject.getOwner().getId(),
                savedProject.getStatus(),
                savedProject.getStartDateTime(),
                savedProject.getEndDateTime(),
                usernames
        );
    }

    public List<Project> findAllProjectsInRange(LocalDateTime start, LocalDateTime end) {
        return projectRepository.findAllByStartDateTimeBetween(start, end);
    }

    public Project updateProject(Long id, Project project) {
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isPresent()) {
            Project updatedProject = existingProject.get();
            updatedProject.setName(project.getName());
            updatedProject.setIntro(project.getIntro());
            updatedProject.setStatus(project.getStatus());
            updatedProject.setStartDateTime(project.getStartDateTime());
            updatedProject.setEndDateTime(project.getEndDateTime());
            return projectRepository.save(updatedProject);
        }
        return null;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}