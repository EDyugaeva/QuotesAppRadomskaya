package com.example.voteservice.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VoteDto {
    private Long author;
    private LocalDate createdAt;
    private int grade;
}
