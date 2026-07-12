package com.example.JobSerivce.Job.DTO;

import com.example.JobSerivce.Job.external.Company;
import com.example.JobSerivce.Job.external.Review;
import lombok.Data;

import java.util.List;

@Data
public class JobDto {
//        private Job job;
        private Long id;
        private String title;
        private String description;
        private String maxsalary;
        private String minsalary;
        private String location;
        private Company company;
        private List<Review> reviews;
    }
