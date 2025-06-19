package com.abdulkhaleel.blockchain_voting.vote.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteResponse {
    private Long voteId;
    private Long electionId;
    private Long candidateId;
    private Long voterId;
    private LocalDateTime votedAt;
    private String message;
}
