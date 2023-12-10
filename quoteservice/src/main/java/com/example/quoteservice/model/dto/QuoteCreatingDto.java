package com.example.quoteservice.model.dto;

import lombok.Data;

@Data
public class QuoteCreatingDto {
    private String content;
    private Long userId;
}
