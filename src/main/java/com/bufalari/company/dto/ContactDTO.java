package com.bufalari.company.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Import Schema
import jakarta.validation.constraints.NotBlank; // Usar @NotBlank
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID; // <<<--- IMPORT UUID

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    @Schema(description = "Unique identifier (UUID) of the contact", example = "a1a2a3a4-b5b6-c7c8-d9d0-e1e2e3e4e5e6", readOnly = true)
    private UUID id; // <<<--- Adicionado ID UUID

    @NotBlank(message = "{contact.type.required}")
    @Size(max = 50, message = "{contact.type.size}")
    @Schema(description = "Type of contact", example = "EMAIL_COMERCIAL", allowableValues = {"TELEFONE_FIXO", "TELEFONE_CELULAR", "EMAIL_COMERCIAL", "EMAIL_SUPORTE", "WHATSAPP", "SITE"})
    private String type; // Ex: "TELEFONE_FIXO", "EMAIL_COMERCIAL", etc.

    @NotBlank(message = "{contact.value.required}")
    @Size(max = 255, message = "{contact.value.size}")
    @Schema(description = "The contact value (phone number, email address)", example = "contato@bufalari.com")
    private String value; // Ex: número de telefone ou email
}