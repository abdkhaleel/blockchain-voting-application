package com.abdulkhaleel.blockchain_voting.candidate.services;

import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateRequest;
import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateResponse;
import com.abdulkhaleel.blockchain_voting.candidate.model.Candidate;
import com.abdulkhaleel.blockchain_voting.candidate.repository.CandidateRepository;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.repository.ElectionRepository;
import com.abdulkhaleel.blockchain_voting.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService{
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public CandidateResponse addCandidate(Long electionId, CandidateRequest candidateRequest){
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with the id: " + electionId));

        Candidate candidate = Candidate.builder()
                .name(candidateRequest.getName())
                .party(candidateRequest.getParty())
                .manifesto(candidateRequest.getManifesto())
                .election(election)
                .build();

        Candidate savedCandidate = candidateRepository.save(candidate);

        return CandidateResponse.formEntity(savedCandidate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidateResponse> getCandidatesByElection(Long electionId){
        if(!electionRepository.existsById(electionId)){
            throw new ResourceNotFoundException("Election not found with the id: "+ electionId);
        }
        return candidateRepository.findByElectionId(electionId).stream()
                .map(CandidateResponse::formEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateResponse getCandidateById(Long candidateId){
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        return CandidateResponse.formEntity(candidate);
    }

    @Override
    @Transactional
    public CandidateResponse updateCandidate(Long electionId, Long candidateId, CandidateRequest candidateRequest){
        Candidate candidate = candidateRepository.findByIdAndElectionId(candidateId, electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with id " + candidateId + "not found in the election " + electionId));

        candidate.setName(candidateRequest.getName());
        candidate.setParty(candidateRequest.getParty());
        candidate.setManifesto(candidateRequest.getManifesto());

        Candidate updatedCandidate = candidateRepository.save(candidate);
        return CandidateResponse.formEntity(updatedCandidate);
    }

    @Override
    @Transactional
    public void deleteCandidate(Long electionId, Long candidateId){
        Candidate candidate = candidateRepository.findByIdAndElectionId(candidateId, electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate with id " + candidateId + "not found in the election " + electionId));

        candidateRepository.delete(candidate);
    }
}
