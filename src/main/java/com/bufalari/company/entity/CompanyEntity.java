package com.bufalari.company.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String businessIdentificationNumber;

    @Embedded
    private AddressEntity address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<ContactEntity> contacts;

    private String mainActivity;

    private LocalDate foundationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private ManagerResponsibleEntity responsible;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private ManagerResponsibleEntity manager;
}
