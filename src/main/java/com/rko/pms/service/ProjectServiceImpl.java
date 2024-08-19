package com.rko.pms.service;

import com.rko.pms.UserUtil;
import com.rko.pms.domain.User;
import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;

import com.rko.pms.projection.ProjectReports;
import com.rko.pms.repository.ProjectRepository;
import com.rko.pms.repository.UserRepository;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserUtil userUtil;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, UserUtil userUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

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
    public ProjectDTO createProject(ProjectDTO projectDTO, String username) {
        if (projectDTO.getProjectMemberUsernames() != null && projectDTO.getProjectMemberUsernames().size() > 5) {
            throw new IllegalArgumentException("A project cannot have more than 5 members.");
        }

        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setIntro(projectDTO.getIntro());
        project.setStatus(projectDTO.getStatus());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());

        User owner = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        project.setOwner(owner);

        if (projectDTO.getProjectMemberUsernames() != null && !projectDTO.getProjectMemberUsernames().isEmpty()) {
            Set<User> projectMembers = projectDTO.getProjectMemberUsernames().stream().map(memberUsername -> userRepository.findByUsername(memberUsername).orElseThrow(() -> new IllegalArgumentException("User not found: " + memberUsername))).collect(Collectors.toSet());
            project.setProjectMembers(projectMembers);
        }

        Project savedProject = projectRepository.save(project);

        Set<String> usernames = savedProject.getProjectMembers().stream().map(User::getUsername).collect(Collectors.toSet());


        return new ProjectDTO(savedProject.getId(), savedProject.getName(), savedProject.getIntro(), savedProject.getStatus(), savedProject.getStartDate(), savedProject.getEndDate(), usernames, savedProject.getOwner().getUsername());
    }

    public List<ProjectDTO> findAllProjectsInRange(LocalDate start, LocalDate end) {
        List<Project> projects = projectRepository.findAllByStartDateBetween(start, end);
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
        dto.setOwner(project.getOwner().getUsername());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setProjectMemberUsernames(project.getProjectMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        return dto;
    }


    public ProjectDTO findProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        ProjectDTO dtos = new ProjectDTO();
        dtos.setId(project.getId());
        dtos.setName(project.getName());
        dtos.setIntro(project.getIntro());
        dtos.setOwner(project.getOwner().getUsername());
        dtos.setStatus(project.getStatus());
        dtos.setStartDate(project.getStartDate());
        dtos.setEndDate(project.getEndDate());
        dtos.setProjectMemberUsernames(project.getProjectMembers().stream().map(User::getUsername).collect(Collectors.toSet()));
        return dtos;
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        validateProjectOwnership(id);

        Project existingProject = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        existingProject.setName(projectDTO.getName());
        existingProject.setIntro(projectDTO.getIntro());
        existingProject.setStatus(projectDTO.getStatus());
        existingProject.setStartDate(projectDTO.getStartDate());
        existingProject.setEndDate(projectDTO.getEndDate());

        Set<User> projectMembers = projectDTO.getProjectMemberUsernames().stream().map(username -> userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username))).collect(Collectors.toSet());
        existingProject.setProjectMembers(projectMembers);

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


    public String getProjectsReport() throws IOException, JRException {
        List<ProjectReports> projects = projectRepository.getReports();
        String path = "E:\\Reports";
        File file = new ClassPathResource("reports/project_report.jrxml").getFile();
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(projects);
        String reportFileName = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
        String reportPath = path + "/report[" + reportFileName + "].pdf";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Project Report");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath);

        return reportPath;
    }
}