package com.bufalari.company.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManagerResponsibleDTO {

    @NotNull(message = "{managerResponsible.code.required}")
    private String code;

    @NotNull(message = "{managerResponsible.name.required}")
    private String name;

    @NotNull(message = "{managerResponsible.position.required}")
    private String position;

    @NotNull(message = "{managerResponsible.phone.required}")
    private String phone;

    @Email(message = "{managerResponsible.email.invalid}")
    private String email;
}
