package com.abdulkhaleel.blockchain_voting.election.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ElectionResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDate endDate;
    private ElectionStatus status;
    private boolean isPublic;
    private boolean allowRevote;
    private LocalDateTime createdAt;
}
