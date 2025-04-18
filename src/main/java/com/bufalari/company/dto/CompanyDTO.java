package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompanyDTO {

    private Long id; // The ID will be generated automatically

    @NotNull(message = "{company.name.required}")
    private String name;

    @NotNull(message = "{company.bin.required}")
    @Size(min = 9, max = 15, message = "{company.bin.size}")
    private String businessIdentificationNumber;

    @NotNull(message = "{company.address.required}")
    private AddressDTO address;

    @NotNull(message = "{company.contacts.required}")
    private List<ContactDTO> contacts;

    @NotNull(message = "{company.mainActivity.required}")
    private String mainActivity;

    @NotNull(message = "{company.foundationDate.required}")
    private LocalDate foundationDate;

    @NotNull(message = "{company.responsible.required}")
    private ManagerResponsibleDTO responsible;

    @NotNull(message = "{company.manager.required}")
    private ManagerResponsibleDTO manager;

    private String googleMapsLink;
}
