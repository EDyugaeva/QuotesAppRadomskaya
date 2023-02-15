package com.example.voteservice.controller;

import com.example.voteservice.model.dto.VoteDto;
import com.example.voteservice.model.dto.VoteResultDto;
import com.example.voteservice.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Controller to post votes and get information about it and results
 */
@RestController
@Slf4j
@RequestMapping(path = "/vote")
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(path = "/up")
    public VoteResultDto upVote(@RequestParam Long quoteId,
                                @RequestParam Long userId) {
        log.info("Upvoting quote {} from user {} ", quoteId, userId);
        return voteService.upVote(quoteId, userId);
    }

    @PostMapping(path = "/down")
    public VoteResultDto downVote(@RequestParam Long quoteId,
                                  @RequestParam Long userId) {
        log.info("Down quote {} from user {} ", quoteId, userId);
        return voteService.downVote(quoteId, userId);
    }


    @GetMapping(path = "/graph/{id}")
    public Map<String, Integer> getGraph(@PathVariable Long id) {
        log.info("try to get graph votes to quote with id = {}", id);
        return voteService.getGraphForQuote(id);
    }

    @GetMapping("/result/{id}")
    public VoteResultDto getVoteResult(@PathVariable Long id) {
        log.info("Try to get result vote to quote with id = {}", id);
        return voteService.getVoteFromQuote(id);
    }

    @GetMapping("/quote/{id}")
    public List<VoteDto> getAllVote(@PathVariable Long id) {
        log.info("Try to all votes from quote = {}", id) ;
        return voteService.getVotesFromQuote(id);
    }

}
