package com.example.voteservice.controller;

import com.example.voteservice.model.entity.Vote;
import com.example.voteservice.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping(path = "/vote")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(path = "/up")
    public Vote upVote(@RequestParam Long quoteId,
                       @RequestParam long authorId) {
        log.info("Upvoting quote " + authorId);
        return voteService.upVote(quoteId, authorId);
    }

    @PostMapping(path = "/down")
    public Vote downVote(@RequestParam Long quoteId,
                         @RequestParam long authorId) {
        log.info("Down quote " + authorId);
        return voteService.downVote(quoteId, authorId);
    }


    @GetMapping(path = "/graph")
    public Map<String, Integer> getGraph(@RequestParam Long quoteId) {
        log.info("try to get graph");
        return voteService.getGraphForQuote(quoteId);
    }

}
