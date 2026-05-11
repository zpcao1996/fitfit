package com.fitfit.research.repository;

import com.fitfit.research.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTitleContaining(String title);
    List<Project> findByStatus(String status);
    List<Project> findByCategory(String category);
    List<Project> findByPrincipalInvestigatorContaining(String pi);

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countByStatus();

    @Query("SELECT p.category, COUNT(p) FROM Project p GROUP BY p.category")
    List<Object[]> countByCategory();

    @Query("SELECT COALESCE(SUM(p.funding), 0) FROM Project p")
    Double totalFunding();

    long countByStatusIn(List<String> statuses);

    List<Project> findTop5ByOrderByCreatedAtDesc();
}
