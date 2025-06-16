package com.abdulkhaleel.blockchain_voting.candidate.services;

import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateRequest;
import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateResponse;

import java.util.List;

public interface CandidateService {
    CandidateResponse addCandidate(Long electionId, CandidateRequest candidateRequest);
    List<CandidateResponse> getCandidatesByElection(Long electionId);
    CandidateResponse getCandidateById(Long candidateId);
    CandidateResponse updateCandidate(Long electionId, Long candidateId, CandidateRequest candidateRequest);
    void deleteCandidate(Long electionId, Long candidateId);
}
