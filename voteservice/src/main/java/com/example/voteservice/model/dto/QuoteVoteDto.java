package com.example.voteservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuoteVoteDto {
    private Long quoteId;
    private Long voteId;
}
