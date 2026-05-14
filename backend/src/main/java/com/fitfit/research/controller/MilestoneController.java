package com.fitfit.research.controller;

import com.fitfit.research.entity.Milestone;
import com.fitfit.research.repository.MilestoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {

    private final MilestoneRepository milestoneRepository;

    public MilestoneController(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    @GetMapping
    public List<Milestone> list(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status) {
        if (projectId != null) return milestoneRepository.findByProjectId(projectId);
        if (status != null && !status.isBlank()) return milestoneRepository.findByStatus(status);
        return milestoneRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Milestone> get(@PathVariable Long id) {
        return milestoneRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Milestone create(@RequestBody Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Milestone updated) {
        return milestoneRepository.findById(id).map(m -> {
            if (updated.getTitle() != null) m.setTitle(updated.getTitle());
            if (updated.getDescription() != null) m.setDescription(updated.getDescription());
            if (updated.getDueDate() != null) m.setDueDate(updated.getDueDate());
            if (updated.getCompletedDate() != null) m.setCompletedDate(updated.getCompletedDate());
            if (updated.getStatus() != null) m.setStatus(updated.getStatus());
            if (updated.getProgress() != null) m.setProgress(updated.getProgress());
            if (updated.getProjectId() != null) m.setProjectId(updated.getProjectId());
            if (updated.getProjectTitle() != null) m.setProjectTitle(updated.getProjectTitle());
            return ResponseEntity.ok(milestoneRepository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (milestoneRepository.existsById(id)) {
            milestoneRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }
}
