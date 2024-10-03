package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompanyDTO {

    private Long id; // The ID will be generated automatically

    @NotNull(message = "Company name is required")
    private String name;

    @NotNull(message = "Business Identification Number is required")
    @Size(min = 9, max = 15, message = "Business Identification Number must be between 9 and 15 characters")
    private String businessIdentificationNumber;

    @NotNull(message = "Address is required")
    private AddressDTO address;

    @NotNull(message = "At least one contact is required")
    private List<ContactDTO> contacts;

    @NotNull(message = "Main activity is required")
    private String mainActivity;

    @NotNull(message = "Foundation date is required")
    private LocalDate foundationDate;

    @NotNull(message = "Responsible person is required")
    private ManagerResponsibleDTO responsible;

    @NotNull(message = "Manager is required")
    private ManagerResponsibleDTO manager;

    private String googleMapsLink;
}
