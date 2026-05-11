package com.fitfit.research.controller;

import com.fitfit.research.entity.FundingRecord;
import com.fitfit.research.repository.FundingRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funding")
public class FundingController {

    private final FundingRecordRepository fundingRecordRepository;

    public FundingController(FundingRecordRepository fundingRecordRepository) {
        this.fundingRecordRepository = fundingRecordRepository;
    }

    @GetMapping
    public List<FundingRecord> list(@RequestParam(required = false) Long projectId) {
        if (projectId != null) {
            return fundingRecordRepository.findByProjectId(projectId);
        }
        return fundingRecordRepository.findAll();
    }

    @PostMapping
    public FundingRecord create(@RequestBody FundingRecord record) {
        return fundingRecordRepository.save(record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (fundingRecordRepository.existsById(id)) {
            fundingRecordRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        }
        return ResponseEntity.notFound().build();
    }
}
