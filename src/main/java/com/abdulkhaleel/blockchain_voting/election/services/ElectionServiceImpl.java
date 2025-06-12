package com.abdulkhaleel.blockchain_voting.election.services;

import com.abdulkhaleel.blockchain_voting.election.dto.CreateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.dto.UpdateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import com.abdulkhaleel.blockchain_voting.election.repository.ElectionRepository;
import com.abdulkhaleel.blockchain_voting.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ElectionServiceImpl implements ElectionService{

    private final ElectionRepository electionRepository;

    @Override
    public Election createElection(CreateElectionRequest request){
        Election election = new Election();
        election.setTitle(request.getTitle());
        election.setDescription(request.getDescription());
        election.setStartDate(request.getStartDate());
        election.setEndDate(LocalDate.from(request.getEndDate()));
        election.setPublic(request.isPublic());
        election.setAllowRevote(request.isAllowRevote());
        election.setStatus(ElectionStatus.PENDING);
        election.setCreatedAt(LocalDateTime.now());

        return electionRepository.save(election);
    }

    @Override
    public Page<Election> getAllElections(Pageable pageable){
        return electionRepository.findAll(pageable);
    }

    @Override
    public Election getElectionById(Long electionId) {
        return electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Election not found with Id: " + electionId
                ));
    }

    @Override
    public Election updateElection(Long electionId, UpdateElectionRequest request) {
        Election election = getElectionById(electionId);

        election.setTitle(request.getTitle());
        election.setDescription(request.getDescription());
        election.setStartDate(request.getStartDate());
        election.setEndDate(LocalDate.from(request.getEndDate()));
        election.setStatus(request.getStatus());
        election.setPublic(request.isPublic());
        election.setAllowRevote(request.isAllowRevote());

        return electionRepository.save(election);
    }

    @Override
    public void deleteElection(Long electionId) {
        if(!electionRepository.existsById(electionId)){
            throw new ResourceNotFoundException("Election not found with the Id: " + electionId);
        }
        electionRepository.deleteById(electionId);
    }


}
