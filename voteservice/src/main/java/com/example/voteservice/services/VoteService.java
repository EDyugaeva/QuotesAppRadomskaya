package com.example.voteservice.services;



import com.example.voteservice.model.entity.Vote;

import java.util.List;
import java.util.Map;

public interface VoteService {

    Vote upVote(Long quoteId, Long userId);

    Vote downVote(Long quoteId, Long userId);

    void deleteVote(Vote vote);

    int getResultVoteFromQuoteId(long quoteId);

    List<Long> getTopTen();

    List<Long> getWorseTen();

    Map<String, Integer> getGraphForQuote(Long quoteId);
}
