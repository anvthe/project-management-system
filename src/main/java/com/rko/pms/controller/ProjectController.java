package com.rko.pms.controller;

import com.rko.pms.domain.Project;
import com.rko.pms.dto.ProjectDTO;
import com.rko.pms.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectServiceImpl projectService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO project) {
        try {
            ProjectDTO createdProject = projectService.createProject(project);
            return ResponseEntity.ok(createdProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Project> getProjects(@RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return projectService.findAllProjectsInRange(startDate, endDate);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
