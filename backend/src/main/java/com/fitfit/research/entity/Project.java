package com.fitfit.research.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(unique = true, length = 50)
    private String projectCode;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(length = 20)
    private String status = "申请中";

    @Column(nullable = false, length = 100)
    private String principalInvestigator;

    @Column(length = 100)
    private String department;

    private Double funding = 0.0;

    @Column(length = 200)
    private String fundingSource;

    @Column(length = 20)
    private String startDate;

    @Column(length = 20)
    private String endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String members;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrincipalInvestigator() { return principalInvestigator; }
    public void setPrincipalInvestigator(String pi) { this.principalInvestigator = pi; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Double getFunding() { return funding; }
    public void setFunding(Double funding) { this.funding = funding; }
    public String getFundingSource() { return fundingSource; }
    public void setFundingSource(String fundingSource) { this.fundingSource = fundingSource; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
