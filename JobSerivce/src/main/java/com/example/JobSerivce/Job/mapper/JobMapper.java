package com.example.JobSerivce.Job.mapper;

import com.example.JobSerivce.Job.DTO.JobDto;
import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.external.Company;
import com.example.JobSerivce.Job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDto mappertoJOb(Job job, Company company, List<Review> reviews){
        JobDto jobwithcompany=new JobDto();
        jobwithcompany.setId(job.getId());
        jobwithcompany.setDescription(job.getDescription());
        jobwithcompany.setTitle(job.getTitle());
        jobwithcompany.setMaxsalary(job.getMaxsalary());
        jobwithcompany.setMinsalary(job.getMinsalary());
        jobwithcompany.setLocation(job.getMaxsalary());
        jobwithcompany.setCompany(company);
        jobwithcompany.setReviews(reviews);
        return jobwithcompany;
    }
}
