package com.example.JobSerivce.Job.DTO;

import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.external.Company;
import lombok.Data;

@Data
public class JobWIthCompany {
        private Job job;
        private Company company;
    }
