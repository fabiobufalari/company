package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ContactDTO {

    @NotNull(message = "{contact.type.required}")
    private String type; // E.g., "phone", "email"

    @NotNull(message = "{contact.value.required}")
    private String value; // E.g., phone number or email
}
