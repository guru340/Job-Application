package com.example.JobSerivce.Job;

import com.example.JobSerivce.Job.Job;

import java.util.List;

public interface JobService {
    List<Job> fetchallJobs();
    void createJob(Job job);

    Job getJobById(Long id);
    boolean deleteJobById(Long id);

    boolean updatejob(Long id, Job updatejob);
}
