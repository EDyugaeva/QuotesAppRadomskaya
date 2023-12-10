package com.example.quoteservice.model.dto;

import lombok.Data;

@Data
public class QuoteVoteDto {
    private Long quoteId;
    private Long voteId;
}
