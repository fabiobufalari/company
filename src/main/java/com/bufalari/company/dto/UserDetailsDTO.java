package com.bufalari.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO for user details received from the authentication service.
 * Defines the structure expected by company-service.
 * DTO para detalhes do usuário recebidos do serviço de autenticação.
 * Define a estrutura esperada pelo company-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    // Campos que este serviço *pode* precisar do authentication-service
    // Fields this service *might* need from the authentication-service

    private UUID id;
    private String username;
    private String email; // Opcional, pode ser útil para logs
    private List<String> roles; // Essencial para validação de token/autorização

    // Campos como firstName, lastName podem ser omitidos se não forem usados aqui
    // private String firstName;
    // private String lastName;

    // NUNCA incluir a senha (password)
}