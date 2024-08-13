package com.rko.pms.controller;

import com.rko.pms.ValidationUtil;
import com.rko.pms.dto.ProjectDTO;
import com.rko.pms.service.ProjectServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectServiceImpl projectService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtil.getErrorMessages(result));
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            ProjectDTO createdProject = projectService.createProject(projectDTO, username);
            return ResponseEntity.ok(createdProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public List<ProjectDTO> getProjects(@RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return projectService.findAllProjectsInRange(startDate, endDate);
    }

    @PutMapping("edit/{id}")
    public ProjectDTO updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return projectService.updateProject(id, projectDTO);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
