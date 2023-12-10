package com.example.quoteservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuoteUserDto {
    private Long quoteId;
    private Long userId;
}
