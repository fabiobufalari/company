package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AddressDTO {

    @NotNull(message = "Street name is required")
    private String street;

    @NotNull(message = "Street number is required")
    private String number;

    private String complement;
    private String neighbourhood;

    @NotNull(message = "City is required")
    private String city;

    @NotNull(message = "Province is required")
    private String province;

    @NotNull(message = "Postal code is required")
    private String postalCode;

    private String googleMapsLink;
}
