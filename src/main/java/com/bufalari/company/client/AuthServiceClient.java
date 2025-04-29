package com.bufalari.company.client;

// 1. Importe o DTO LOCAL deste serviço (criado abaixo)
import com.bufalari.company.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communication with the authentication service FROM company-service.
 * Cliente Feign para comunicação com o serviço de autenticação A PARTIR DO company-service.
 */
// 2. Use a propriedade correta do application.yml (auth.service.url)
@FeignClient(name = "auth-service-client-company", url = "${auth.service.url}") // Nome único e URL correta
public interface AuthServiceClient {

    /**
     * Retrieves user details by username from the authentication service.
     * (Needed by the dummy UserDetailsService for basic validation structure).
     * Busca os detalhes do usuário por nome de usuário no serviço de autenticação.
     * (Necessário pelo UserDetailsService dummy para estrutura básica de validação).
     */
    // 3. Path relativo à URL base (http://localhost:8083/api)
    @GetMapping("/users/username/{username}")
    UserDetailsDTO getUserByUsername(@PathVariable("username") String username);

     /**
     * Retrieves user details by user ID (UUID) from the authentication service.
     * (Optional, but potentially useful in the future).
     * Busca os detalhes do usuário por ID (UUID) no serviço de autenticação.
     * (Opcional, mas potencialmente útil no futuro).
     */
     // 3. Path relativo à URL base (http://localhost:8083/api)
     @GetMapping("/users/{id}")
     UserDetailsDTO getUserById(@PathVariable("id") String userId); // Passa UUID como String

}