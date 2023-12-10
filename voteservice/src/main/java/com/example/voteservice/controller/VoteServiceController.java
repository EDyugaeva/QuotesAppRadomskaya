package com.example.voteservice.controller;

import com.example.voteservice.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Controller to send response from quotes
 */
@RestController
@Slf4j
@RequestMapping(path = "/service")
public class VoteServiceController {

    private final VoteService voteService;
    public VoteServiceController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping(path = "/best")
    public List<Long> getTopTen() {
        log.info("Get top 10 quotes");
        return voteService.getTopTen();
    }

    @GetMapping(path = "/worst")
    public List<Long> getWorstTen() {
        log.info("Get worst 10 quotes");
        return voteService.getWorseTen();
    }
}