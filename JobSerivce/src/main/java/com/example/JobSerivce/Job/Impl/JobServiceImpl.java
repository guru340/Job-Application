package com.example.JobSerivce.Job.Impl;


import com.example.JobSerivce.Job.DTO.JobWIthCompany;
import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.JobRepo;
import com.example.JobSerivce.Job.JobService;
import com.example.JobSerivce.Job.external.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
//    List<Job> jobList=new ArrayList<>();
    private final JobRepo jobRepo;
    private Long NextId =1L;
    @Override
    public List<JobWIthCompany> fetchallJobs() {
        List<Job> jobs = jobRepo.findAll();
        List<JobWIthCompany> jobWithCompanyDTOs = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        for (Job job : jobs) {
            JobWIthCompany jobWithCompanyDTO = new JobWIthCompany();
            jobWithCompanyDTO.setJob(job);

            Company company = restTemplate.getForObject(
                    "http://localhost:8082/companies/" + job.getCompanyId(),
                    Company.class);
            jobWithCompanyDTO.setCompany(company);

            jobWithCompanyDTOs.add(jobWithCompanyDTO);
        }

        return jobWithCompanyDTOs;
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
