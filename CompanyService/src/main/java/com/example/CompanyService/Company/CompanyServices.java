package com.example.CompanyService.Company;

import java.util.List;

public interface CompanyServices {

     List<Company> fetchallCompany();
    boolean updateCompany(Company company, Long id);



    void CreateCompany(Company company);

    boolean deleteCompany(Long id);
    Company getcomapnybyId(Long id);
}
