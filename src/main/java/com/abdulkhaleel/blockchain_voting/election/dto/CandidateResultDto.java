package com.abdulkhaleel.blockchain_voting.election.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResultDto {
    private Long candidateId;
    private String name;
    private Long voteCount;
}
