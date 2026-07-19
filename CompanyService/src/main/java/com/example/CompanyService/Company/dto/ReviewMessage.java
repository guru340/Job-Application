package com.example.CompanyService.Company.dto;

import lombok.Data;

@Data

public class ReviewMessage {
    private Long id;
    private String title;
    private String Description;
    private double rating;
    private Long companyId;
}
