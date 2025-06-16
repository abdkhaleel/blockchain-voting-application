package com.abdulkhaleel.blockchain_voting.candidate.repository;

import com.abdulkhaleel.blockchain_voting.candidate.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByElectionId(Long electionId);

    Optional<Candidate> findByIdAndElectionId(Long id, Long electionId);
}
