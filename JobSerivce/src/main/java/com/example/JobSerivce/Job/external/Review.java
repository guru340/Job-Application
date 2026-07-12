package com.example.JobSerivce.Job.external;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private String title;
    private String Description;
    private double rating;
    private Long companyId;
}
