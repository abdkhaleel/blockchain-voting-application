package com.abdulkhaleel.blockchain_voting.voter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoterResponse {
    private Long id;
    private String username;
    private String email;
}
