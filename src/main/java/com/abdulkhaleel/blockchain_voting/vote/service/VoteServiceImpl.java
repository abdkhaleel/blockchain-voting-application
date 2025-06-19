package com.abdulkhaleel.blockchain_voting.vote.service;

import com.abdulkhaleel.blockchain_voting.candidate.model.Candidate;
import com.abdulkhaleel.blockchain_voting.candidate.repository.CandidateRepository;
import com.abdulkhaleel.blockchain_voting.election.dto.CandidateResultDto;
import com.abdulkhaleel.blockchain_voting.election.dto.ElectionResultsResponse;
import com.abdulkhaleel.blockchain_voting.election.model.Election;
import com.abdulkhaleel.blockchain_voting.election.model.ElectionStatus;
import com.abdulkhaleel.blockchain_voting.election.repository.ElectionRepository;
import com.abdulkhaleel.blockchain_voting.exception.ResourceNotFoundException;
import com.abdulkhaleel.blockchain_voting.security.services.UserDetailsImpl;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import com.abdulkhaleel.blockchain_voting.user.model.User;
import com.abdulkhaleel.blockchain_voting.user.repository.UserRepository;
import com.abdulkhaleel.blockchain_voting.vote.dto.CandidateVoteCount;
import com.abdulkhaleel.blockchain_voting.vote.dto.CastVoteRequest;
import com.abdulkhaleel.blockchain_voting.vote.dto.HasVotedResponse;
import com.abdulkhaleel.blockchain_voting.vote.dto.VoteResponse;
import com.abdulkhaleel.blockchain_voting.vote.model.Vote;
import com.abdulkhaleel.blockchain_voting.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{
    private final UserRepository userRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public VoteResponse castVote(CastVoteRequest castVoteRequest, UserDetailsImpl currentUser){
        Long userId = currentUser.getId();

        User voter = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the id: " + userId));

        Election election = electionRepository.findById(castVoteRequest.getElectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with the id: " + castVoteRequest.getElectionId()));

        Candidate candidate = candidateRepository.findById(castVoteRequest.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with the id: " + castVoteRequest.getCandidateId()));

        if(election.getStatus() != ElectionStatus.ACTIVE){
            throw new IllegalStateException("Voting is only allowed for the Active elections.");
        }

        if(!election.getEligibleVoters().contains(voter)){
            throw new AccessDeniedException("You are not eligible to vote this election");
        }

        if(!Objects.equals(candidate.getElection().getId(), election.getId())){
            throw new IllegalArgumentException("The selected candidate is not part of this election");
        }

        voteRepository.findByVoterIdAndElectionId(userId, election.getId())
                .ifPresent(v -> {
                    throw new IllegalStateException("You have already cast your vote in this election.");
                });

        Vote vote = Vote.builder()
                .voter(voter)
                .election(election)
                .candidate(candidate)
                .build();

        Vote savedVote = (Vote) voteRepository.save(vote);

        return VoteResponse.builder()
                .voteId(savedVote.getId())
                .voterId(savedVote.getVoter().getId())
                .electionId(savedVote.getElection().getId())
                .candidateId(savedVote.getCandidate().getId())
                .votedAt(savedVote.getVotedAt())
                .message("Vote casted successfully!")
                .build();
    }

    @Override
    public HasVotedResponse checkIfVoted(Long electionId, UserDetailsImpl currentUser){
        boolean hasVoted = voteRepository.findByVoterIdAndElectionId(currentUser.getId(), electionId)
                .isPresent();
        return new HasVotedResponse(hasVoted);
    }

    @Override
    @Transactional
    public MessageResponse refractVote(Long electionId, UserDetailsImpl currentUser){
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with the id: " + electionId));

        if(!election.isAllowRevote()){
            throw new IllegalStateException("This election does not allow votes to be retracted.");
        }

        Vote vote = voteRepository.findByVoterIdAndElectionId(currentUser.getId(), electionId)
                .orElseThrow(() -> new ResourceNotFoundException("No vote found to you in this election to retract."));

        voteRepository.delete(vote);

        return new MessageResponse("Your vote has been retracted successfully.");
    }

    @Override
    public ElectionResultsResponse getElectionResult(Long electionId){
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with the id: "));

        List<CandidateVoteCount> voteCounts = voteRepository.countVotesByElection(electionId);

        Map<Long, Long> candidateIdToVoteCountMap = voteCounts.stream()
                .collect(Collectors.toMap(CandidateVoteCount::getCandidateId, CandidateVoteCount::getVoteCount));

        List<CandidateResultDto> candidateResults = election.getCandidates().stream()
                .map(candidate -> CandidateResultDto.builder()
                        .candidateId(candidate.getId())
                        .name(candidate.getName())
                        .voteCount(candidateIdToVoteCountMap.getOrDefault(candidate.getId(), 0L))
                        .build())
                .collect(Collectors.toList());

        return ElectionResultsResponse.builder()
                .electionId(election.getId())
                .name(election.getTitle())
                .status(election.getStatus())
                .startTime(election.getStartDate())
                .endTime(election.getEndDate())
                .results(candidateResults)
                .build();
    }
}
