package com.bufalari.company;


import com.bufalari.company.companyConverter.CompanyConverter;
import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.repository.CompanyRepository;
import com.bufalari.company.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyConverter companyConverter;

    @InjectMocks
    private CompanyService companyService;

    private CompanyDTO companyDTO;
    private CompanyEntity companyEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        companyDTO = new CompanyDTO();
        companyDTO.setId(123L); // Ajustado para verificar o ID
        companyDTO.setName("Company Name");
        companyDTO.setBusinessIdentificationNumber("00.000.000/0000-00");

        companyEntity = new CompanyEntity();
        companyEntity.setId(123L); // Ajustado para verificar o ID
        companyEntity.setName("Company Name");
        companyEntity.setBusinessIdentificationNumber("00.000.000/0000-00");
    }

    @Test
    void testCreateCompany() {
        when(companyConverter.dtoToEntity(any(CompanyDTO.class))).thenReturn(companyEntity);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyEntity);
        when(companyConverter.entityToDTO(any(CompanyEntity.class))).thenReturn(companyDTO);

        CompanyDTO result = companyService.createCompany(companyDTO);

        assertEquals(123L, result.getId()); // Verificando o ID da empresa
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void testGetCompanyById() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(companyEntity));
        when(companyConverter.entityToDTO(any(CompanyEntity.class))).thenReturn(companyDTO);

        Optional<CompanyDTO> result = companyService.getCompanyById(1L);

        assertEquals(123L, result.get().getId()); // Verificando o ID da empresa
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCompany() {
        when(companyRepository.existsById(anyLong())).thenReturn(true);
        when(companyConverter.dtoToEntity(any(CompanyDTO.class))).thenReturn(companyEntity);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyEntity);
        when(companyConverter.entityToDTO(any(CompanyEntity.class))).thenReturn(companyDTO);

        CompanyDTO result = companyService.updateCompany(1L, companyDTO);

        assertEquals(123L, result.getId()); // Verificando o ID da empresa
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void testDeleteCompany() {
        companyService.deleteCompany(1L);
        verify(companyRepository, times(1)).deleteById(1L);
    }
}
