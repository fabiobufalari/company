package com.bufalari.company.entity;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
@Table(name = "contacts")
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private String type;  // E.g., "phone", "email"
    private String value; // E.g., phone number or email
}
