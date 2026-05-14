package com.fitfit.research.repository;

import com.fitfit.research.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByTitleContaining(String title);
    List<Publication> findByType(String type);
    List<Publication> findByProjectId(Long projectId);

    @Query("SELECT p.type, COUNT(p) FROM Publication p GROUP BY p.type")
    List<Object[]> countByType();
}
