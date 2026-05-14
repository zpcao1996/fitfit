package com.fitfit.research.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "publications")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(length = 200)
    private String authors;

    @Column(length = 200)
    private String journal;

    @Column(length = 20)
    private String publishDate;

    @Column(length = 50)
    private String doi;

    private Long projectId;

    @Column(length = 100)
    private String projectTitle;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column(length = 50)
    private String impactFactor;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
    public String getJournal() { return journal; }
    public void setJournal(String journal) { this.journal = journal; }
    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
    public String getDoi() { return doi; }
    public void setDoi(String doi) { this.doi = doi; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }
    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }
    public String getImpactFactor() { return impactFactor; }
    public void setImpactFactor(String impactFactor) { this.impactFactor = impactFactor; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
