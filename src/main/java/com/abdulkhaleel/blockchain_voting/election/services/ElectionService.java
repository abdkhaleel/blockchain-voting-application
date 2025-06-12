package com.abdulkhaleel.blockchain_voting.election.services;

import com.abdulkhaleel.blockchain_voting.election.dto.CreateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.dto.UpdateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElectionService {
    Election createElection(CreateElectionRequest request);
    Page<Election> getAllElections(Pageable pageable);
    Election getElectionById(Long electionId);
    Election updateElection(Long electionId, UpdateElectionRequest request);
    void deleteElection(Long electionId);
}
