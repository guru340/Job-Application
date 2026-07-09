package com.example.JobSerivce.Job.Impl;


import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.JobRepo;
import com.example.JobSerivce.Job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
//    List<Job> jobList=new ArrayList<>();
    private final JobRepo jobRepo;
    private Long NextId =1L;
    @Override
    public List<Job> fetchallJobs() {
        return jobRepo.findAll();
    }

    @Override
    public void createJob(Job job) {

        jobRepo.save(job);
    }

    @Override
    public Job getJobById(Long id) {
       return jobRepo.findById(id).orElse(null);

    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean updatejob(Long id, Job updatejob) {
        Optional<Job> optionalJob=jobRepo.findById(id);

            if(optionalJob.isPresent()){
                Job job=optionalJob.get();
                job.setId(updatejob.getId());
                job.setDescription(updatejob.getDescription());
                job.setTitle(updatejob.getTitle());
                job.setLocation(updatejob.getLocation());
                job.setMaxsalary(updatejob.getMaxsalary());
                job.setMinsalary(updatejob.getMinsalary());
                return true;

            }

        return false;
    }
}
