package com.bufalari.company.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ManagerResponsibleDTO {

    @NotNull(message = "Code is required")
    private String code;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Position is required")
    private String position;

    @NotNull(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;
}
