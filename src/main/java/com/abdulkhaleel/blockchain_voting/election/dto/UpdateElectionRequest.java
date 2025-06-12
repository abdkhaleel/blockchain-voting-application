package com.abdulkhaleel.blockchain_voting.election.dto;

import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UpdateElectionRequest {

    @NotBlank(message = "Election title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "Status cannot be null")
    private ElectionStatus status;

    private boolean isPublic;
    private boolean allowRevote;
}
