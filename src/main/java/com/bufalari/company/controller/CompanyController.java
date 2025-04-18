package com.bufalari.company.controller;

import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável pelo gerenciamento de empresas.
 * REST controller responsible for managing companies.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Cria uma nova empresa no sistema.
     * Creates a new company in the system.
     */
    @PostMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Descomente para proteger por perfil
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        CompanyDTO created = companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Lista todas as empresas registradas.
     * Lists all registered companies.
     */
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    /**
     * Busca uma empresa pelo ID.
     * Finds a company by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        Optional<CompanyDTO> result = companyService.getCompanyById(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Atualiza os dados de uma empresa existente.
     * Updates an existing company's data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id,
                                                    @Valid @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updated = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Remove uma empresa pelo ID.
     * Deletes a company by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
