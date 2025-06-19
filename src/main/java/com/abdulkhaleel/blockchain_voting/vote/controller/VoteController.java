package com.abdulkhaleel.blockchain_voting.vote.controller;

import com.abdulkhaleel.blockchain_voting.security.services.UserDetailsImpl;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import com.abdulkhaleel.blockchain_voting.vote.dto.CastVoteRequest;
import com.abdulkhaleel.blockchain_voting.vote.dto.HasVotedResponse;
import com.abdulkhaleel.blockchain_voting.vote.dto.VoteResponse;
import com.abdulkhaleel.blockchain_voting.vote.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VoteResponse> casteVote(
            @Valid @RequestBody CastVoteRequest castVoteRequest,
            @AuthenticationPrincipal UserDetailsImpl currentUser){
        VoteResponse voteResponse = voteService.castVote(castVoteRequest, currentUser);
        return ResponseEntity.ok(voteResponse);
    }

    @GetMapping("/check/{electionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HasVotedResponse> checkIfUserHasVoted(
            @PathVariable Long electionId,
            @AuthenticationPrincipal UserDetailsImpl currentUser){
        HasVotedResponse response = voteService.checkIfVoted(electionId, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{electionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponse> retractVote(
            @PathVariable Long electionId,
            @AuthenticationPrincipal UserDetailsImpl currentUser){
        MessageResponse response = voteService.refractVote(electionId, currentUser);
        return ResponseEntity.ok(response);
    }
}
