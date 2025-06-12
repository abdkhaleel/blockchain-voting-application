package com.abdulkhaleel.blockchain_voting.election.repository;

import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {
    List <Election> findByStatus(ElectionStatus status);

    Election getById(Long electionId);
}
