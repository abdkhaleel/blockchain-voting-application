package com.abdulkhaleel.blockchain_voting.vote.service;

import com.abdulkhaleel.blockchain_voting.security.services.UserDetailsImpl;
import com.abdulkhaleel.blockchain_voting.user.dto.MessageResponse;
import com.abdulkhaleel.blockchain_voting.vote.dto.CastVoteRequest;
import com.abdulkhaleel.blockchain_voting.vote.dto.HasVotedResponse;
import com.abdulkhaleel.blockchain_voting.vote.dto.VoteResponse;

public interface VoteService {
    VoteResponse castVote(CastVoteRequest castVoteRequest, UserDetailsImpl currentUser);
    HasVotedResponse checkIfVoted(Long electionId, UserDetailsImpl currentUser);
    MessageResponse refractVote(Long electionId, UserDetailsImpl currentUser);
}
