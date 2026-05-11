package com.fitfit.research.controller;

import com.fitfit.research.entity.Project;
import com.fitfit.research.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        if (keyword != null && !keyword.isBlank()) {
            return projectRepository.findByTitleContaining(keyword);
        }
        if (status != null && !status.isBlank()) {
            return projectRepository.findByStatus(status);
        }
        if (category != null && !category.isBlank()) {
            return projectRepository.findByCategory(category);
        }
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Project create(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Project updated) {
        return projectRepository.findById(id).map(p -> {
            if (updated.getTitle() != null) p.setTitle(updated.getTitle());
            if (updated.getProjectCode() != null) p.setProjectCode(updated.getProjectCode());
            if (updated.getCategory() != null) p.setCategory(updated.getCategory());
            if (updated.getStatus() != null) p.setStatus(updated.getStatus());
            if (updated.getPrincipalInvestigator() != null) p.setPrincipalInvestigator(updated.getPrincipalInvestigator());
            if (updated.getDepartment() != null) p.setDepartment(updated.getDepartment());
            if (updated.getFunding() != null) p.setFunding(updated.getFunding());
            if (updated.getFundingSource() != null) p.setFundingSource(updated.getFundingSource());
            if (updated.getStartDate() != null) p.setStartDate(updated.getStartDate());
            if (updated.getEndDate() != null) p.setEndDate(updated.getEndDate());
            if (updated.getDescription() != null) p.setDescription(updated.getDescription());
            if (updated.getMembers() != null) p.setMembers(updated.getMembers());
            return ResponseEntity.ok(projectRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }
}
