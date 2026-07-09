package com.example.CompanyService.Company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor

public class CompanyController {

    private final CompanyServices companyServices;

    @GetMapping
    public ResponseEntity<List<Company>> getallCompanies(){
        return ResponseEntity.ok(companyServices.fetchallCompany());
    }

    @PostMapping
    public ResponseEntity<String> createCompany(@RequestBody Company company){
        companyServices.CreateCompany(company);
        return new ResponseEntity<>("Company Create Successfully",HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id,@RequestBody Company company){
        boolean updateCompany= companyServices.updateCompany(company,id);
        if (updateCompany){
            return new ResponseEntity<>("Company Updated SucessfUlly",HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        boolean deletecompany=companyServices.deleteCompany(id);
        if(deletecompany){
            return new ResponseEntity<>("Delete SuccessFully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Company> getcompanyByID(@PathVariable Long id){
        Company company=companyServices.getcomapnybyId(id);
        if (company!=null){
            return new ResponseEntity<>(company, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
