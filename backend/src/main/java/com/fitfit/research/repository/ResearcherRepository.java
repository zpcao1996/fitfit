package com.fitfit.research.repository;

import com.fitfit.research.entity.Researcher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResearcherRepository extends JpaRepository<Researcher, Long> {
    List<Researcher> findByNameContaining(String name);
    List<Researcher> findByDepartment(String department);
}
