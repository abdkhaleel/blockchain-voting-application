package com.abdulkhaleel.blockchain_voting.vote.repository;

import com.abdulkhaleel.blockchain_voting.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByVoterIdAndElectionId(Long voterId, Long electionId);
}
