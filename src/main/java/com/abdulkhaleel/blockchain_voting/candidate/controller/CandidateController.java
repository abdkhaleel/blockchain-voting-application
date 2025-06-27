package com.abdulkhaleel.blockchain_voting.candidate.controller;

import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateRequest;
import com.abdulkhaleel.blockchain_voting.candidate.dto.CandidateResponse;
import com.abdulkhaleel.blockchain_voting.candidate.services.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/elections/{electionId}/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponse> addCandidate(@PathVariable Long electionId, @Valid @RequestBody CandidateRequest candidateRequest){
        CandidateResponse newCandidate = candidateService.addCandidate(electionId, candidateRequest);
        return new ResponseEntity<>(newCandidate, HttpStatus.CREATED);
    }

    @GetMapping("/elections/{electionId}/candidates")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByElectionId(@PathVariable Long electionId){
        List<CandidateResponse> candidates = candidateService.getCandidatesByElection(electionId);

        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/candidates/{candidateId}")
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Long candidateId){
        CandidateResponse candidate = candidateService.getCandidateById(candidateId);
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/elections/{electionId}/candidates/{candidateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @PathVariable Long electionId,
            @PathVariable Long candidateId,
            @Valid @RequestBody CandidateRequest candidateRequest){

        CandidateResponse candidate = candidateService.updateCandidate(electionId, candidateId, candidateRequest);
        return ResponseEntity.ok(candidate);
    }

    @DeleteMapping("/elections/{electionId}/candidates/{candidateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCandidate(
            @PathVariable Long electionId,
            @PathVariable Long candidateId){

        candidateService.deleteCandidate(electionId, candidateId);
        return ResponseEntity.noContent().build();
    }
}
