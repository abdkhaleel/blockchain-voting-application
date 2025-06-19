package com.abdulkhaleel.blockchain_voting.vote.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateVoteCount {
    private Long candidateId;
    private Long voteCount;
}
