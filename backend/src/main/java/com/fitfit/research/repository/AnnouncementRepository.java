package com.fitfit.research.repository;

import com.fitfit.research.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByType(String type);
    List<Announcement> findByTitleContaining(String title);
    List<Announcement> findByPinnedTrueOrderByCreatedAtDesc();
    List<Announcement> findAllByOrderByPinnedDescCreatedAtDesc();
}
