package com.bufalari.company.exception;

import java.util.UUID; // <<<--- IMPORT UUID

/**
 * Exceção personalizada para quando a empresa não é encontrada.
 * Custom exception thrown when a company is not found.
 */
public class CompanyNotFoundException extends RuntimeException {

    /**
     * Construtor padrão.
     * Default constructor.
     */
    public CompanyNotFoundException() {
        super("Company not found");
    }

    /**
     * Construtor que aceita o UUID da empresa não encontrada.
     * Constructor that accepts the UUID of the company not found.
     * @param id O UUID da empresa. / The UUID of the company.
     */
    public CompanyNotFoundException(UUID id) { // <<<--- Alterado para UUID
        super("Company with ID " + id + " not found");
    }

    /**
     * Construtor que aceita uma mensagem customizada.
     * Constructor that accepts a custom message.
     * @param message A mensagem de erro. / The error message.
     */
    public CompanyNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor que aceita uma mensagem e a causa original.
     * Constructor that accepts a message and the original cause.
     * @param message A mensagem de erro. / The error message.
     * @param cause A causa original. / The original cause.
     */
    public CompanyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}