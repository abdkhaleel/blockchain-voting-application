package com.abdulkhaleel.blockchain_voting.vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CastVoteRequest {
    @NotNull(message = "Election ID is required")
    private Long electionId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
}
