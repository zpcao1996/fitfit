package com.fitfit.research.controller;

import com.fitfit.research.entity.Announcement;
import com.fitfit.research.repository.AnnouncementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @GetMapping
    public List<Announcement> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type) {
        if (keyword != null && !keyword.isBlank()) return announcementRepository.findByTitleContaining(keyword);
        if (type != null && !type.isBlank()) return announcementRepository.findByType(type);
        return announcementRepository.findAllByOrderByPinnedDescCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> get(@PathVariable Long id) {
        return announcementRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Announcement create(@RequestBody Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Announcement updated) {
        return announcementRepository.findById(id).map(a -> {
            if (updated.getTitle() != null) a.setTitle(updated.getTitle());
            if (updated.getContent() != null) a.setContent(updated.getContent());
            if (updated.getType() != null) a.setType(updated.getType());
            if (updated.getAuthor() != null) a.setAuthor(updated.getAuthor());
            a.setPinned(updated.isPinned());
            if (updated.getPublishDate() != null) a.setPublishDate(updated.getPublishDate());
            return ResponseEntity.ok(announcementRepository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (announcementRepository.existsById(id)) {
            announcementRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }
}
