package com.abdulkhaleel.blockchain_voting.election.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateElectionRequest {

    @NotBlank(message = "Election title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    private boolean isPublic = true;
    private boolean allowRevote = false;
}
