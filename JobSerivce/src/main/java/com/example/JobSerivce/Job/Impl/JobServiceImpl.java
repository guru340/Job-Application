package com.example.JobSerivce.Job.Impl;


import com.example.JobSerivce.Job.DTO.JobDto;
import com.example.JobSerivce.Job.Job;
import com.example.JobSerivce.Job.JobRepo;
import com.example.JobSerivce.Job.JobService;
import com.example.JobSerivce.Job.clients.CompanyClient;
import com.example.JobSerivce.Job.clients.ReviewClient;
import com.example.JobSerivce.Job.external.Company;
import com.example.JobSerivce.Job.external.Review;
import com.example.JobSerivce.Job.mapper.JobMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private CompanyClient companyClient;

    @Autowired
    private ReviewClient reviewClient;
    int attempt=0;

    @Override
//    @CircuitBreaker(name = "companyBreaker",fallbackMethod = "fallbackForJob")
//    @Retry(name = "companyBreaker",fallbackMethod = "fallbackForJob")

    @RateLimiter(name = "companyBreaker",fallbackMethod = "fallbackForJob")

    public List<JobDto> fetchallJobs() {
        System.out.println("Attempted"+ ++attempt);
        List<Job> jobs = jobRepo.findAll();
        List<JobDto> jobWithCompanyDTOs = new ArrayList<>();

        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());




    }

    private JobDto convertToDto(Job job){

        Company company=companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

//        Company company = restTemplate.getForObject(
//                "http://COMPANYSERVICE:8082/companies/" + job.getCompanyId(),
//                Company.class);

//     ResponseEntity<List<Review>> Reviewresponse= restTemplate.exchange("http://REVIEWSERVICES:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
//                });
//
//     List<Review> reviews=Reviewresponse.getBody();
        JobDto jobDto = JobMapper.mappertoJOb(job,company,reviews);
//        jobDto.setCompany(company);




        return jobDto;
    }

    @Override
    public void createJob(Job job) {

        jobRepo.save(job);
    }

    @Override

    public JobDto getJobById(Long id) {
       Job job= jobRepo.findById(id).orElse(null);
        return convertToDto(job);
    }
    public List<String> fallbackForJob(Throwable t) {
        List<String> list=new ArrayList<>();
        list.add( "Fallback data: Service is currently unavailable.");
        return list;
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
