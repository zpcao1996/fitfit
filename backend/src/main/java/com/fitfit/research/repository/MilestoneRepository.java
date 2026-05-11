package com.fitfit.research.repository;

import com.fitfit.research.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProjectId(Long projectId);
    List<Milestone> findByStatus(String status);

    @Query("SELECT m.status, COUNT(m) FROM Milestone m GROUP BY m.status")
    List<Object[]> countByStatus();
}
