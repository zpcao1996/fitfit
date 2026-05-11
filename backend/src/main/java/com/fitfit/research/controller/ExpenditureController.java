package com.fitfit.research.controller;

import com.fitfit.research.entity.Expenditure;
import com.fitfit.research.repository.ExpenditureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureRepository expenditureRepository;

    public ExpenditureController(ExpenditureRepository expenditureRepository) {
        this.expenditureRepository = expenditureRepository;
    }

    @GetMapping
    public List<Expenditure> list(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        if (projectId != null) return expenditureRepository.findByProjectId(projectId);
        if (status != null && !status.isBlank()) return expenditureRepository.findByStatus(status);
        if (category != null && !category.isBlank()) return expenditureRepository.findByCategory(category);
        return expenditureRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expenditure> get(@PathVariable Long id) {
        return expenditureRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Expenditure create(@RequestBody Expenditure expenditure) {
        return expenditureRepository.save(expenditure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Expenditure updated) {
        return expenditureRepository.findById(id).map(e -> {
            if (updated.getProjectId() != null) e.setProjectId(updated.getProjectId());
            if (updated.getProjectTitle() != null) e.setProjectTitle(updated.getProjectTitle());
            if (updated.getAmount() != null) e.setAmount(updated.getAmount());
            if (updated.getCategory() != null) e.setCategory(updated.getCategory());
            if (updated.getDescription() != null) e.setDescription(updated.getDescription());
            if (updated.getApplicant() != null) e.setApplicant(updated.getApplicant());
            if (updated.getExpenseDate() != null) e.setExpenseDate(updated.getExpenseDate());
            if (updated.getStatus() != null) e.setStatus(updated.getStatus());
            return ResponseEntity.ok(expenditureRepository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (expenditureRepository.existsById(id)) {
            expenditureRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        double totalApproved = Optional.ofNullable(expenditureRepository.totalApprovedAmount()).orElse(0.0);
        Map<String, Double> byCategory = new LinkedHashMap<>();
        for (Object[] row : expenditureRepository.sumByCategory()) {
            byCategory.put((String) row[0], (Double) row[1]);
        }
        return Map.of("totalApproved", totalApproved, "byCategory", byCategory, "total", expenditureRepository.count());
    }
}
