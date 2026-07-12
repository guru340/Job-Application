package com.example.JobSerivce.Job;

import com.example.JobSerivce.Job.DTO.JobWIthCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {


    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobWIthCompany>> getallUser(){
        return ResponseEntity.ok(jobService.fetchallJobs());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody Job job){
        jobService.createJob(job);
        return  new ResponseEntity<>("Job Added SuccessFully",HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobWIthCompany> getjobbyId(@PathVariable  Long id){
        JobWIthCompany jobwithCompany=jobService.getJobById(id);
        if (jobwithCompany!=null){
            return new ResponseEntity<>(jobwithCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean deleted=jobService.deleteJobById(id);
        if (deleted){
            return new ResponseEntity<>("Job Deleted Sucessfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id ,@RequestBody Job updatejob){
        boolean update=jobService.updatejob(id,updatejob);
        if (update){
            return new ResponseEntity<>("Job Updated Successfully",HttpStatus.OK);
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
