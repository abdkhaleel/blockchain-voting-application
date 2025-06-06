package com.abdulkhaleel.blockchain_voting.election.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "elections")
@Data
@NoArgsConstructor
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ElectionStatus status = ElectionStatus.PENDING;

    private boolean isPublic;
    private boolean allowRevote;

    private LocalDateTime createdAt = LocalDateTime.now();
}
