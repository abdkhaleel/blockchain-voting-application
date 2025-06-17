package com.abdulkhaleel.blockchain_voting.voter.services;

import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.repository.ElectionRepository;
import com.abdulkhaleel.blockchain_voting.exception.ResourceNotFoundException;
import com.abdulkhaleel.blockchain_voting.user.model.User;
import com.abdulkhaleel.blockchain_voting.user.repository.UserRepository;
import com.abdulkhaleel.blockchain_voting.voter.dto.EligibilityResponse;
import com.abdulkhaleel.blockchain_voting.voter.dto.VoterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoterServiceImpl implements VoterService{
    private final ElectionRepository electionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addVoterToElection(Long electionId, Long userId){
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id: " + electionId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        election.getEligibleVoters().add(user);
        electionRepository.save(election);
    }

    @Override
    @Transactional
    public void removeVoterFromElection(Long electionId, Long userId){
        Election election = electionRepository.findByIdWithVoters(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id: " + electionId));
        boolean removed = election.getEligibleVoters().removeIf(user -> user.getId().equals(userId));

        if (!removed) {
            throw new ResourceNotFoundException("User with id " + userId + " was not found in the eligibility list for this election.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoterResponse> getEligibleVotersForElection(Long electionId, Pageable pageable){

        if (!electionRepository.existsById(electionId)) {
            throw new ResourceNotFoundException("Election not found with id: " + electionId);
        }

        Page<User> userPage = userRepository.findByEligibleElections_Id(electionId, pageable);
        return userPage.map(user -> new VoterResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Override
    @Transactional(readOnly = true)
    public EligibilityResponse checkVoterEligibility(Long electionId, Long userId){
        boolean isEligible = electionRepository.existsByIdAndEligibleVoters_Id(electionId, userId);

        return new EligibilityResponse(isEligible);
    }

}
