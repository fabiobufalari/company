package com.bufalari.company.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;



@Embeddable
@Data
public class AddressEntity {

    private String street;
    private String number;
    private String complement;
    private String neighbourhood;
    private String city;
    private String province;
    private String postalCode;
}
