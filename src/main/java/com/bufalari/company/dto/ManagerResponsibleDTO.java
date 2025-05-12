package com.bufalari.company.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Import Schema
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // Usar @NotBlank
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID; // <<<--- IMPORT UUID

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerResponsibleDTO {

    @Schema(description = "Unique identifier (UUID) of the manager/responsible person", example = "b1b2b3b4-c5c6-d7d8-e9e0-f1f2f3f4f5f6", readOnly = true)
    private UUID id; // <<<--- Adicionado ID UUID

    @NotBlank(message = "{managerResponsible.code.required}") // Pode ser código interno, matrícula, etc.
    @Size(max = 50, message = "{managerResponsible.code.size}")
    @Schema(description = "Internal code or identifier for the person", example = "MGR-001")
    private String code;

    @NotBlank(message = "{managerResponsible.name.required}")
    @Size(max = 255, message = "{managerResponsible.name.size}")
    @Schema(description = "Full name of the manager or responsible person", example = "João da Silva")
    private String name;

    @NotBlank(message = "{managerResponsible.position.required}")
    @Size(max = 100, message = "{managerResponsible.position.size}")
    @Schema(description = "Job title or position", example = "Diretor Financeiro")
    private String position; // Cargo

    @NotBlank(message = "{managerResponsible.phone.required}")
    @Size(max = 50, message = "{managerResponsible.phone.size}")
    @Schema(description = "Contact phone number", example = "+55 11 98765-4321")
    private String phone;

    @NotBlank(message = "{managerResponsible.email.required}") // Email geralmente é obrigatório
    @Email(message = "{managerResponsible.email.invalid}")
    @Size(max = 255, message = "{managerResponsible.email.size}")
    @Schema(description = "Contact email address", example = "joao.silva@bufalari.com")
    private String email;
}