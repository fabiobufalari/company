package com.bufalari.company.controller;

import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation; // Importar anotações OpenAPI
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag; // Importar Tag
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Usar Lombok para injeção
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; // Importar MediaType
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // Para URI de criação

import java.net.URI; // Importar URI
import java.util.List;
import java.util.Optional;
import java.util.UUID; // <<<--- IMPORT UUID

/**
 * Controlador REST responsável pelo gerenciamento de empresas (usando UUID).
 * REST controller responsible for managing companies (using UUID).
 */
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor // Injeção via construtor pelo Lombok
@Tag(name = "Company Management", description = "Endpoints for managing company data") // Tag Swagger/OpenAPI
@SecurityRequirement(name = "bearerAuth") // Assume global security requirement
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    /**
     * Cria uma nova empresa no sistema.
     * Creates a new company in the system.
     */
    @Operation(summary = "Create Company", description = "Registers a new company.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MANAGER')") // Exemplo: Apenas Admin ou Manager podem criar
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        log.info("Request received to create company: {}", companyDTO.getName());
        CompanyDTO created = companyService.createCompany(companyDTO);
        // Cria a URI para o recurso criado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()) // Usa o ID retornado
                .toUri();
        log.info("Company created with ID {} at location {}", created.getId(), location);
        return ResponseEntity.created(location).body(created); // Retorna 201 Created
    }

    /**
     * Lista todas as empresas registradas.
     * Lists all registered companies.
     */
    @Operation(summary = "Get All Companies", description = "Retrieves a list of all registered companies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()") // Qualquer usuário autenticado pode listar
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        log.debug("Request received to list all companies.");
        List<CompanyDTO> companies = companyService.getAllCompanies();
        log.info("Returning {} companies.", companies.size());
        return ResponseEntity.ok(companies);
    }

    /**
     * Busca uma empresa pelo seu UUID.
     * Finds a company by its UUID.
     */
    @Operation(summary = "Get Company by ID", description = "Retrieves a specific company by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()") // Qualquer usuário autenticado pode buscar por ID
    public ResponseEntity<CompanyDTO> getCompanyById(
            @Parameter(description = "UUID of the company to retrieve") @PathVariable UUID id) { // <<<--- UUID
        log.debug("Request received to get company by ID: {}", id);
        Optional<CompanyDTO> result = companyService.getCompanyById(id);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> { // Usa lambda para logar apenas se não encontrar
                    log.warn("Company not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Atualiza os dados de uma empresa existente pelo seu UUID.
     * Updates an existing company's data by its UUID.
     */
    @Operation(summary = "Update Company", description = "Updates details of an existing company by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MANAGER')") // Exemplo de roles para update
    public ResponseEntity<CompanyDTO> updateCompany(
            @Parameter(description = "UUID of the company to update") @PathVariable UUID id, // <<<--- UUID
            @Valid @RequestBody CompanyDTO companyDTO) {
        log.info("Request received to update company ID: {}", id);
        // Opcional: verificar se ID no path e no body coincidem
        if (companyDTO.getId() != null && !companyDTO.getId().equals(id)) {
            log.warn("Path ID {} does not match body ID {}. Using path ID for update.", id, companyDTO.getId());
            // Pode lançar Bad Request ou simplesmente ignorar o ID do body
        }
        CompanyDTO updated = companyService.updateCompany(id, companyDTO);
        log.info("Company updated successfully for ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Remove uma empresa pelo seu UUID.
     * Deletes a company by its UUID.
     */
    @Operation(summary = "Delete Company", description = "Deletes a company by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., company has dependencies)") // Exemplo
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Apenas Admin pode deletar
    public ResponseEntity<Void> deleteCompany(
            @Parameter(description = "UUID of the company to delete") @PathVariable UUID id) { // <<<--- UUID
        log.info("Request received to delete company ID: {}", id);
        companyService.deleteCompany(id); // O serviço deve tratar Not Found
        log.info("Company deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}