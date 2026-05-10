package com.fitfit.research.controller;

import com.fitfit.research.entity.Researcher;
import com.fitfit.research.repository.ResearcherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/researchers")
public class ResearcherController {

    private final ResearcherRepository researcherRepository;

    public ResearcherController(ResearcherRepository researcherRepository) {
        this.researcherRepository = researcherRepository;
    }

    @GetMapping
    public List<Researcher> list(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return researcherRepository.findByNameContaining(keyword);
        }
        return researcherRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Researcher> get(@PathVariable Long id) {
        return researcherRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Researcher create(@RequestBody Researcher researcher) {
        return researcherRepository.save(researcher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Researcher updated) {
        return researcherRepository.findById(id).map(r -> {
            if (updated.getName() != null) r.setName(updated.getName());
            if (updated.getEmployeeId() != null) r.setEmployeeId(updated.getEmployeeId());
            if (updated.getTitle() != null) r.setTitle(updated.getTitle());
            if (updated.getDepartment() != null) r.setDepartment(updated.getDepartment());
            if (updated.getEmail() != null) r.setEmail(updated.getEmail());
            if (updated.getPhone() != null) r.setPhone(updated.getPhone());
            if (updated.getResearchField() != null) r.setResearchField(updated.getResearchField());
            return ResponseEntity.ok(researcherRepository.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (researcherRepository.existsById(id)) {
            researcherRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }
}
