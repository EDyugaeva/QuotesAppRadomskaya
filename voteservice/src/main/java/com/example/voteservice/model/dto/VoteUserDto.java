package com.example.voteservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteUserDto {
    private Long userId;
    private Long voteId;
}
