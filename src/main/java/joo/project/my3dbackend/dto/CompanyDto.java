package joo.project.my3dbackend.dto;

import joo.project.my3dbackend.domain.Company;

public record CompanyDto(Long id, String companyName, String homepage) {

    public static CompanyDto fromEntity(Company company) {
        return new CompanyDto(company.getId(), company.getCompanyName(), company.getHomepage());
    }

    public Company toEntity() {
        return Company.of(companyName, homepage);
    }
}
