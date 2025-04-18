package com.bufalari.company.exception;

// Exceção personalizada para quando a empresa não é encontrada / Custom exception thrown when a company is not found
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException() {
        super("Company not found");
    }
    // Construtor opcional com ID da empresa / Optional constructor with company ID
    public CompanyNotFoundException(Long id) {
        super("Company not found"); // ou incluir ID: "Company with ID " + id + " not found"
    }
}
