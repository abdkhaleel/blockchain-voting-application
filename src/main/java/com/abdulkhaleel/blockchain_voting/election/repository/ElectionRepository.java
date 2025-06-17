package com.abdulkhaleel.blockchain_voting.election.repository;

import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    List <Election> findByStatus(ElectionStatus status);

    Election getById(Long electionId);

    boolean existsByIdAndEligibleVoters_Id(Long electionId, Long userId);

    @Query("SELECT e FROM Election e LEFT JOIN FETCH e.eligibleVoters WHERE e.id = :electionId")
    Optional<Election> findByIdWithVoters(@Param("electionId") Long electionId);
}
