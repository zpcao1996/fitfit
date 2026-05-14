package com.fitfit.research.controller;

import com.fitfit.research.entity.Publication;
import com.fitfit.research.repository.PublicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationRepository publicationRepository;

    public PublicationController(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @GetMapping
    public List<Publication> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long projectId) {
        if (keyword != null && !keyword.isBlank()) return publicationRepository.findByTitleContaining(keyword);
        if (type != null && !type.isBlank()) return publicationRepository.findByType(type);
        if (projectId != null) return publicationRepository.findByProjectId(projectId);
        return publicationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> get(@PathVariable Long id) {
        return publicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Publication create(@RequestBody Publication pub) {
        return publicationRepository.save(pub);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Publication updated) {
        return publicationRepository.findById(id).map(p -> {
            if (updated.getTitle() != null) p.setTitle(updated.getTitle());
            if (updated.getType() != null) p.setType(updated.getType());
            if (updated.getAuthors() != null) p.setAuthors(updated.getAuthors());
            if (updated.getJournal() != null) p.setJournal(updated.getJournal());
            if (updated.getPublishDate() != null) p.setPublishDate(updated.getPublishDate());
            if (updated.getDoi() != null) p.setDoi(updated.getDoi());
            if (updated.getProjectId() != null) p.setProjectId(updated.getProjectId());
            if (updated.getProjectTitle() != null) p.setProjectTitle(updated.getProjectTitle());
            if (updated.getAbstractText() != null) p.setAbstractText(updated.getAbstractText());
            if (updated.getImpactFactor() != null) p.setImpactFactor(updated.getImpactFactor());
            return ResponseEntity.ok(publicationRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (publicationRepository.existsById(id)) {
            publicationRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Long> byType = new java.util.LinkedHashMap<>();
        for (Object[] row : publicationRepository.countByType()) {
            byType.put((String) row[0], (Long) row[1]);
        }
        return Map.of("total", publicationRepository.count(), "byType", byType);
    }
}
