package com.bufalari.company.security;

import com.bufalari.company.client.AuthServiceClient; // Importar cliente local
import com.bufalari.company.dto.UserDetailsDTO; // Importar DTO local
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import feign.FeignException; // Importar FeignException

import java.util.Collections;
import java.util.stream.Collectors;


@Service("companyUserDetailsService") // Nome específico
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService { // Nome da classe ajustado

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final AuthServiceClient authServiceClient; // Injetar o cliente local

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         log.debug("[Company] Attempting to load user details for username: {}", username);
        UserDetailsDTO userDetailsDTO;
        try {
            // Chama o Feign client para buscar dados básicos (sem senha)
            userDetailsDTO = authServiceClient.getUserByUsername(username);

            if (userDetailsDTO == null) {
                log.warn("[Company] User details DTO is null for username: {}", username);
                throw new UsernameNotFoundException("User not found via auth service: " + username);
            }

             log.info("[Company] Successfully loaded user details via auth service for username: {}", username);

            // --- AJUSTE AQUI ---
            // Construir UserDetails sem depender do password do DTO
            return new User(
                    userDetailsDTO.getUsername(),
                    "", // <<< Passar string vazia ou placeholder
                    userDetailsDTO.getRoles() != null ?
                            userDetailsDTO.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                    .collect(Collectors.toList())
                            : Collections.emptyList()
            );

        } catch (FeignException.NotFound e) {
            log.warn("[Company] User not found via auth service for username: {}. Feign status: 404", username);
            throw new UsernameNotFoundException("User not found: " + username, e);
        } catch (FeignException e) {
             log.error("[Company] Feign error calling auth service for username: {}. Status: {}, Response: {}",
                     username, e.status(), e.contentUTF8(), e);
            throw new UsernameNotFoundException("Failed to load user details (auth service comm error) for user: " + username, e);
        } catch (Exception e) {
            log.error("[Company] Unexpected error loading user details for username: {}", username, e);
            throw new UsernameNotFoundException("Unexpected error loading user details for user: " + username, e);
        }
    }
}