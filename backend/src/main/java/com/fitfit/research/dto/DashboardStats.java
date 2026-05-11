package com.fitfit.research.dto;

import java.util.List;
import java.util.Map;

public record DashboardStats(
    long totalProjects,
    long activeProjects,
    double totalFunding,
    long totalResearchers,
    Map<String, Long> projectsByStatus,
    Map<String, Long> projectsByCategory,
    List<Map<String, Object>> recentProjects
) {}
