package com.abdulkhaleel.blockchain_voting.election.services;

import com.abdulkhaleel.blockchain_voting.blockchain.service.BlockchainService;
import com.abdulkhaleel.blockchain_voting.candidate.repository.CandidateRepository;
import com.abdulkhaleel.blockchain_voting.election.dto.CandidateResultDto;
import com.abdulkhaleel.blockchain_voting.election.dto.CreateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.dto.ElectionResultsResponse;
import com.abdulkhaleel.blockchain_voting.election.dto.UpdateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionResponse;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import com.abdulkhaleel.blockchain_voting.election.repository.ElectionRepository;
import com.abdulkhaleel.blockchain_voting.exception.ResourceNotFoundException;
import com.abdulkhaleel.blockchain_voting.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ElectionServiceImpl implements ElectionService{
    private final BlockchainService blockchainService;
    private final VoteService voteService;
    private final CandidateRepository candidateRepository;
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
    @Override
    @Transactional
    public ElectionResponse closeAndAnchorElection(Long electionId){
        Election election = getElectionById(electionId);
        if(election.getStatus() == ElectionStatus.COMPLETED){
            throw new IllegalStateException("Election is already closed and anchored.");
        }
        ElectionResultsResponse results = voteService.getElectionResult(electionId);

        StringBuilder resultString = new StringBuilder();

        resultString.append("electionId:").append(results.getElectionId()).append(";");
        results.getResults().stream()
                .sorted(Comparator.comparing(CandidateResultDto::getCandidateId))
                .forEach(r -> resultString
                        .append(r.getCandidateId()).append(";")
                        .append(r.getVoteCount()).append(";"));

        String resultsHash = createSha256Hash(resultString.toString());

        blockchainService.addBlock(resultsHash);

        election.setStatus(ElectionStatus.COMPLETED);
        election.setAnchorHash(resultsHash);
        Election savedElection = electionRepository.save(election);

        return ElectionResponse.formEntity(election);
    }

    private String createSha256Hash(String input) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for(byte b: encodedHash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Could not create hash ", e);
        }
    }
}
