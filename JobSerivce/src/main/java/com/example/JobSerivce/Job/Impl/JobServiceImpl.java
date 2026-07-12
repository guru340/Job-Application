package com.example.JobSerivce.Job.Impl;


import com.example.JobSerivce.Job.DTO.JobWIthCompany;
import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.JobRepo;
import com.example.JobSerivce.Job.JobService;
import com.example.JobSerivce.Job.external.Company;
import com.example.JobSerivce.Job.mapper.JobMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
//    List<Job> jobList=new ArrayList<>();
    private final JobRepo jobRepo;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<JobWIthCompany> fetchallJobs() {
        List<Job> jobs = jobRepo.findAll();
        List<JobWIthCompany> jobWithCompanyDTOs = new ArrayList<>();

        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());




    }

    private JobWIthCompany convertToDto(Job job){


        Company company = restTemplate.getForObject(
                "http://COMPANYSERVICE/companies/" + job.getCompanyId(),
                Company.class);
        JobWIthCompany jobWIthCompany= JobMapper.mappertoJOb(job,company);
        jobWIthCompany.setCompany(company);




        return jobWIthCompany;
    }

    @Override
    public void createJob(Job job) {

        jobRepo.save(job);
    }

    @Override
    public JobWIthCompany getJobById(Long id) {
       Job job= jobRepo.findById(id).orElse(null);
        return convertToDto(job);
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
