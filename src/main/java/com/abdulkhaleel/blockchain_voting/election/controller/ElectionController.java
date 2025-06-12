package com.abdulkhaleel.blockchain_voting.election.controller;

import com.abdulkhaleel.blockchain_voting.election.dto.CreateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.dto.UpdateElectionRequest;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionResponse;
import com.abdulkhaleel.blockchain_voting.election.services.ElectionService;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ElectionResponse> createElection(@Valid @RequestBody CreateElectionRequest request){
        Election election = electionService.createElection(request);
        return new ResponseEntity<>(mapToElectionResponse(election), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ElectionResponse>> getAllElections(Pageable pageable){
        Page<Election> electionPage = electionService.getAllElections(pageable);
        Page<ElectionResponse> electionResponsePage = electionPage.map(this::mapToElectionResponse);
        return ResponseEntity.ok(electionResponsePage);
    }

    @GetMapping("/{electionId}")
    public ResponseEntity<ElectionResponse> getElectionById(@PathVariable Long electionId){
        Election election = electionService.getElectionById(electionId);
        return ResponseEntity.ok(mapToElectionResponse(election));
    }

    @PutMapping("/{electionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ElectionResponse> updateElectionById(@PathVariable Long electionId, @Valid @RequestBody UpdateElectionRequest request){
        Election updateElection = electionService.updateElection(electionId, request);
        return ResponseEntity.ok(mapToElectionResponse(updateElection));
    }

    @DeleteMapping("/{electionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteElection(@PathVariable Long electionId){
        electionService.deleteElection(electionId);
        return ResponseEntity.ok(new MessageResponse("Election deleted successfully!"));
    }

    private ElectionResponse mapToElectionResponse(Election election){
        return ElectionResponse.builder()
                .id(election.getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startDate(election.getStartDate())
                .endDate(election.getEndDate())
                .status(election.getStatus())
                .isPublic(election.isPublic())
                .allowRevote(election.isAllowRevote())
                .createdAt(election.getCreatedAt())
                .build();
    }
}
