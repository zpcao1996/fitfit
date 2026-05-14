package com.fitfit.research.repository;

import com.fitfit.research.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    List<Expenditure> findByProjectId(Long projectId);
    List<Expenditure> findByStatus(String status);
    List<Expenditure> findByCategory(String category);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expenditure e WHERE e.status = '已通过'")
    Double totalApprovedAmount();

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM Expenditure e WHERE e.status = '已通过' GROUP BY e.category")
    List<Object[]> sumByCategory();
}
