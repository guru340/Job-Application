package com.example.CompanyService.Company.Impl;



import com.example.CompanyService.Company.Company;
import com.example.CompanyService.Company.CompanyRepo;
import com.example.CompanyService.Company.CompanyServices;
import com.example.CompanyService.Company.clients.ReviewClient;
import com.example.CompanyService.Company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyImpl implements CompanyServices {

    private final CompanyRepo companyRepo;

    private final ReviewClient reviewClient;

    @Override
    public List<Company> fetchallCompany() {
        return companyRepo.findAll();
    }

    @Override
    public boolean updateCompany(Company company, Long id) {
        Optional<Company> companyOptional=companyRepo.findById(id);
        if(companyOptional.isPresent()){
            Company Tocompany=companyOptional.get();
            Tocompany.setId(company.getId());

            Tocompany.setName(company.getName());
            Tocompany.setDescription(company.getDescription());
            return true;
        }
        return false;
    }

    @Override
    public void CreateCompany(Company company) {
        companyRepo.save(company);
    }

    @Override
    public boolean deleteCompany(Long id) {
        try {
            companyRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Company getcomapnybyId(Long id) {
        return companyRepo.findById(id).orElse(null);
    }

    @Override
    public void updateRating(ReviewMessage reviewMessage) {
        System.out.println(reviewMessage.getCompanyId());
        Company company=companyRepo.findById(reviewMessage.getCompanyId()).orElseThrow(()->new NotFoundException("company not found"+ reviewMessage.getCompanyId()));

        double averageRating=reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepo.save(company);
    }


}
