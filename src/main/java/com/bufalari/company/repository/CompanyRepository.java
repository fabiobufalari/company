package com.bufalari.company.repository;

import com.bufalari.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Adicionar @Repository (opcional, mas boa prática)

import java.util.UUID; // <<<--- IMPORT UUID

/**
 * Repositório JPA para a entidade CompanyEntity (usando UUID como ID).
 * JPA Repository for the CompanyEntity (using UUID as ID).
 */
@Repository // Marca como um componente repositório do Spring
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> { // <<<--- Alterado para UUID
    // Métodos CRUD básicos (save, findById, findAll, deleteById, etc.) são herdados
    // e agora operam com UUID como tipo de ID.

    // Você pode adicionar métodos de consulta personalizados aqui se necessário.
    // Exemplo:
    // Optional<CompanyEntity> findByBusinessIdentificationNumber(String bin);
    // List<CompanyEntity> findByNameContainingIgnoreCase(String name);
}