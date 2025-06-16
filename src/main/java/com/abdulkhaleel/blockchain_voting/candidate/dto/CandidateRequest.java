package com.abdulkhaleel.blockchain_voting.candidate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {
    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Candidate party is required")
    private String party;

    private String manifesto;
}
