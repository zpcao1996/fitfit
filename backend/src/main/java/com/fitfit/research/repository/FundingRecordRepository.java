package com.fitfit.research.repository;

import com.fitfit.research.entity.FundingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FundingRecordRepository extends JpaRepository<FundingRecord, Long> {
    List<FundingRecord> findByProjectId(Long projectId);
}
