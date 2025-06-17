package com.abdulkhaleel.blockchain_voting.voter.services;

import com.abdulkhaleel.blockchain_voting.voter.dto.EligibilityResponse;
import com.abdulkhaleel.blockchain_voting.voter.dto.VoterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoterService {
    void addVoterToElection(Long electionId, Long userId);

    void removeVoterFromElection(Long electionId, Long userId);

    Page<VoterResponse> getEligibleVotersForElection(Long electionId, Pageable pageable);

    EligibilityResponse checkVoterEligibility(Long electionId, Long userId);
}
