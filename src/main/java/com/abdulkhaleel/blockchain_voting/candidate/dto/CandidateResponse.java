package com.abdulkhaleel.blockchain_voting.candidate.dto;

import com.abdulkhaleel.blockchain_voting.candidate.model.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {
    private Long id;
    private String name;
    private String party;
    private String manifesto;
    private Long electionId;

    public static CandidateResponse formEntity(Candidate candidate){
        return CandidateResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .party(candidate.getParty())
                .manifesto(candidate.getManifesto())
                .electionId(candidate.getElection().getId())
                .build();
    }
}
