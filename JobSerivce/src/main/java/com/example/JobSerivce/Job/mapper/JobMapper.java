package com.example.JobSerivce.Job.mapper;

import com.example.JobSerivce.Job.DTO.JobWIthCompany;
import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.external.Company;

public class JobMapper {
    public static  JobWIthCompany mappertoJOb(Job job, Company company){
        JobWIthCompany jobwithcompany=new JobWIthCompany();
        jobwithcompany.setId(job.getId());
        jobwithcompany.setDescription(job.getDescription());
        jobwithcompany.setTitle(job.getTitle());
        jobwithcompany.setMaxsalary(job.getMaxsalary());
        jobwithcompany.setMinsalary(job.getMinsalary());
        jobwithcompany.setLocation(job.getMaxsalary());
        jobwithcompany.setCompany(company);
        return jobwithcompany;
    }
}
