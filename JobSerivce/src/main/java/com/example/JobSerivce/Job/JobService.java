package com.example.JobSerivce.Job;

import com.example.JobSerivce.Job.DTO.JobWIthCompany;
import com.example.JobSerivce.Job.Job;

import java.util.List;

public interface JobService {
    List<JobWIthCompany> fetchallJobs();
    void createJob(Job job);

    Job getJobById(Long id);
    boolean deleteJobById(Long id);

    boolean updatejob(Long id, Job updatejob);
}
