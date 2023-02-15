package com.example.quoteservice.model.dto;

import lombok.Data;

@Data
public class QuoteDto {

    private String content;
    private String dateOfCreation;
    private String dateOfUpdate;

    private String linkOnAuthor;
    private String linkOnVotes;

}
