package com.bufalari.company.service;



import com.bufalari.company.companyConverter.CompanyConverter;
import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyConverter companyConverter;

    public CompanyService(CompanyRepository companyRepository, CompanyConverter companyConverter) {
        this.companyRepository = companyRepository;
        this.companyConverter = companyConverter;
    }

    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        CompanyEntity companyEntity = companyConverter.dtoToEntity(companyDTO);
        CompanyEntity savedCompany = companyRepository.save(companyEntity);
        return companyConverter.entityToDTO(savedCompany);
    }

    public Optional<CompanyDTO> getCompanyById(Long id) {
        return companyRepository.findById(id).map(companyConverter::entityToDTO);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(companyConverter::entityToDTO).toList();
    }

    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("Company not found");
        }
        CompanyEntity companyEntity = companyConverter.dtoToEntity(companyDTO);
        companyEntity.setId(id);
        CompanyEntity updatedCompany = companyRepository.save(companyEntity);
        return companyConverter.entityToDTO(updatedCompany);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
