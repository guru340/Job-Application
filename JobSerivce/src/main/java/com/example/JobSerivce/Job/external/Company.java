package com.example.JobSerivce.Job.external;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

@Data
public class Company {
    private Long id;
    private String name;
    private String description;
}
