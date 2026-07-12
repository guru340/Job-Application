package com.example.JobSerivce.Job;

import com.example.JobSerivce.Job.DTO.JobDto;

import java.util.List;

public interface JobService {
    List<JobDto> fetchallJobs();
    void createJob(Job job);

    JobDto getJobById(Long id);
    boolean deleteJobById(Long id);

    boolean updatejob(Long id, Job updatejob);
}
