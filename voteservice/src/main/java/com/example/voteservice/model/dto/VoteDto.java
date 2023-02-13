package com.example.voteservice.model.dto;

import lombok.Data;

@Data
public class VoteDto {
    private Integer author;
    private String createdAt;
    private Integer quote;
    private Integer grade;


}
