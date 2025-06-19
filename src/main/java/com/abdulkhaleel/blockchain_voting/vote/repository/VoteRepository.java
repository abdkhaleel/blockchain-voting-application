package com.abdulkhaleel.blockchain_voting.vote.repository;

import com.abdulkhaleel.blockchain_voting.vote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import com.abdulkhaleel.blockchain_voting.vote.dto.CandidateVoteCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByVoterIdAndElectionId(Long voterId, Long electionId);

    @Query("SELECT new com.abdulkhaleel.blockchain_voting.vote.dto.CandidateVoteCount(v.candidate.id, COUNT(v)) " +
            "FROM Vote v WHERE v.election.id = :electionId GROUP BY v.candidate.id")
    List<CandidateVoteCount> countVotesByElection(@Param("electionId") Long electionId);

}
