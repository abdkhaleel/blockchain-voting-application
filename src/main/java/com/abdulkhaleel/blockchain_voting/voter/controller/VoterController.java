package com.abdulkhaleel.blockchain_voting.voter.controller;

import com.abdulkhaleel.blockchain_voting.security.services.UserDetailsImpl;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import com.abdulkhaleel.blockchain_voting.voter.dto.EligibilityResponse;
import com.abdulkhaleel.blockchain_voting.voter.dto.VoterResponse;
import com.abdulkhaleel.blockchain_voting.voter.services.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/elections/{electionId}")
@RequiredArgsConstructor
public class VoterController {

    private final VoterService voterService;

    @PostMapping("/voters/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addVoter(
            @PathVariable Long electionId,
            @PathVariable Long userId) {

        voterService.addVoterToElection(electionId, userId);
        return ResponseEntity.ok(new MessageResponse("User successfully made eligible for the election."));
    }

    @DeleteMapping("/voters/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeVoter(
            @PathVariable Long electionId,
            @PathVariable Long userId) {

        voterService.removeVoterFromElection(electionId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/voters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<VoterResponse>> getEligibleVoters(@PathVariable Long electionId, Pageable pageable) {
        Page<VoterResponse> voters = voterService.getEligibleVotersForElection(electionId, pageable);
        return ResponseEntity.ok(voters);
    }

    @GetMapping("/eligibility")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EligibilityResponse> checkMyEligibility(@PathVariable Long electionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long currentUserId = userDetails.getId();

        EligibilityResponse response = voterService.checkVoterEligibility(electionId, currentUserId);

        return ResponseEntity.ok(response);
    }
}
