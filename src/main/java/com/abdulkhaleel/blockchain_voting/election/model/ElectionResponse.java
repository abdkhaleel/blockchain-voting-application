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
    private String anchorHash;
    private boolean allowRevote;
    private LocalDateTime createdAt;

    public static ElectionResponse formEntity(Election election){
        return ElectionResponse.builder()
                .id(election.getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startDate(election.getStartDate())
                .endDate(election.getEndDate())
                .status(election.getStatus())
                .isPublic(election.isPublic())
                .anchorHash(election.getAnchorHash())
                .allowRevote(election.isAllowRevote())
                .createdAt(election.getCreatedAt())
                .build();
    }
}
