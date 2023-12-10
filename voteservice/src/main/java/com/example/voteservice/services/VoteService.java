package com.example.voteservice.services;


import com.example.voteservice.model.dto.UserQuoteDto;
import com.example.voteservice.model.dto.VoteDto;
import com.example.voteservice.model.dto.VoteResultDto;
import com.example.voteservice.model.entity.Vote;

import java.util.List;
import java.util.Map;

public interface VoteService {

    VoteResultDto upVote(UserQuoteDto userQuoteDto);
    VoteResultDto downVote(UserQuoteDto userQuoteDto);
    VoteResultDto getVoteFromQuote(Long quoteId);
    List<VoteDto> getVotesFromQuote(Long quoteId);
    void deleteVote(Vote vote);
    List<Long> getTopTen();
    List<Long> getWorseTen();
    Map<String, Integer> getGraphForQuote(Long quoteId);
}
