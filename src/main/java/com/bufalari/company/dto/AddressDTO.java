package com.bufalari.company.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AddressDTO {

    @NotNull(message = "{address.street.required}")
    private String street;

    @NotNull(message = "{address.number.required}")
    private String number;

    private String complement;
    private String neighbourhood;

    @NotNull(message = "{address.city.required}")
    private String city;

    @NotNull(message = "{address.province.required}")
    private String province;

    @NotNull(message = "{address.postalCode.required}")
    private String postalCode;

    private String googleMapsLink;
}
