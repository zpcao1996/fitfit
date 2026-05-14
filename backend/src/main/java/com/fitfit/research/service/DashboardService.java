package com.fitfit.research.service;

import com.fitfit.research.dto.DashboardStats;
import com.fitfit.research.entity.Project;
import com.fitfit.research.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ResearcherRepository researcherRepository;
    private final PublicationRepository publicationRepository;
    private final MilestoneRepository milestoneRepository;
    private final ExpenditureRepository expenditureRepository;

    public DashboardService(ProjectRepository projectRepository, ResearcherRepository researcherRepository,
                            PublicationRepository publicationRepository, MilestoneRepository milestoneRepository,
                            ExpenditureRepository expenditureRepository) {
        this.projectRepository = projectRepository;
        this.researcherRepository = researcherRepository;
        this.publicationRepository = publicationRepository;
        this.milestoneRepository = milestoneRepository;
        this.expenditureRepository = expenditureRepository;
    }

    public DashboardStats getStats() {
        long total = projectRepository.count();
        long active = projectRepository.countByStatusIn(List.of("进行中", "已立项"));
        double funding = Optional.ofNullable(projectRepository.totalFunding()).orElse(0.0);
        long researchers = researcherRepository.count();
        long publications = publicationRepository.count();
        long milestones = milestoneRepository.count();
        double expenditure = Optional.ofNullable(expenditureRepository.totalApprovedAmount()).orElse(0.0);

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Object[] row : projectRepository.countByStatus()) {
            byStatus.put((String) row[0], (Long) row[1]);
        }

        Map<String, Long> byCategory = new LinkedHashMap<>();
        for (Object[] row : projectRepository.countByCategory()) {
            byCategory.put((String) row[0], (Long) row[1]);
        }

        Map<String, Long> pubsByType = new LinkedHashMap<>();
        for (Object[] row : publicationRepository.countByType()) {
            pubsByType.put((String) row[0], (Long) row[1]);
        }

        List<Map<String, Object>> recent = new ArrayList<>();
        for (Project p : projectRepository.findTop5ByOrderByCreatedAtDesc()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", p.getId());
            m.put("title", p.getTitle());
            m.put("status", p.getStatus());
            m.put("principalInvestigator", p.getPrincipalInvestigator());
            m.put("category", p.getCategory());
            m.put("funding", p.getFunding());
            recent.add(m);
        }

        return new DashboardStats(total, active, funding, researchers, publications, milestones,
                expenditure, byStatus, byCategory, pubsByType, recent);
    }
}
