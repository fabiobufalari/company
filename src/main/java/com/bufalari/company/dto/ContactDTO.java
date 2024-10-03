package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ContactDTO {

    @NotNull(message = "Contact type is required")
    private String type; // E.g., "phone", "email"

    @NotNull(message = "Contact value is required")
    private String value; // E.g., phone number or email
}
