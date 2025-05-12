package com.bufalari.company.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar Schema
import jakarta.validation.Valid; // Importar para validar DTOs aninhados
import jakarta.validation.constraints.NotBlank; // Usar @NotBlank para Strings
import jakarta.validation.constraints.NotEmpty; // Usar @NotEmpty para Listas
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent; // Para data de fundação
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID; // <<<--- IMPORT UUID

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    @Schema(description = "Unique identifier (UUID) of the company", example = "123e4567-e89b-12d3-a456-426614174000", readOnly = true)
    private UUID id; // <<<--- Changed to UUID (Generated automatically)

    @NotBlank(message = "{company.name.required}") // Não nulo e não vazio
    @Size(max = 255, message = "{company.name.size}")
    @Schema(description = "Company legal name", example = "Bufalari Tech Solutions Ltda.")
    private String name;

    @NotBlank(message = "{company.bin.required}")
    @Size(min = 9, max = 20, message = "{company.bin.size}") // Ajustar tamanho conforme necessidade (CNPJ/EIN/etc.)
    @Schema(description = "Business Identification Number (e.g., CNPJ, EIN)", example = "12.345.678/0001-99")
    private String businessIdentificationNumber;

    @NotNull(message = "{company.address.required}")
    @Valid // <<< Adicionar para validar o AddressDTO aninhado
    @Schema(description = "Company's primary address")
    private AddressDTO address;

    @NotEmpty(message = "{company.contacts.required}") // Lista não pode ser vazia
    @Valid // <<< Adicionar para validar cada ContactDTO na lista
    @Schema(description = "List of company contacts (phone, email, etc.)")
    private List<ContactDTO> contacts;

    @NotBlank(message = "{company.mainActivity.required}")
    @Size(max = 255, message = "{company.mainActivity.size}")
    @Schema(description = "Company's main economic activity (CNAE, etc.)", example = "Software development")
    private String mainActivity;

    @NotNull(message = "{company.foundationDate.required}")
    @PastOrPresent(message = "{company.foundationDate.pastOrPresent}") // Data deve ser no passado ou presente
    @Schema(description = "Date the company was founded", example = "2010-03-15")
    private LocalDate foundationDate;

    @NotNull(message = "{company.responsible.required}")
    @Valid // <<< Adicionar para validar o DTO aninhado
    @Schema(description = "Legally responsible person for the company")
    private ManagerResponsibleDTO responsible; // Responsável Legal

    @NotNull(message = "{company.manager.required}")
    @Valid // <<< Adicionar para validar o DTO aninhado
    @Schema(description = "Primary manager or contact person")
    private ManagerResponsibleDTO manager; // Gerente ou Contato Principal

    @Schema(description = "Link to Google Maps for the company address", readOnly = true)
    private String googleMapsLink; // Gerado automaticamente
}