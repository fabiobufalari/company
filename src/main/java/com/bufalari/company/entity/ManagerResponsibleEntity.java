package com.bufalari.company.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "managers_responsibles")
public class ManagerResponsibleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String position;
    private String phone;
    private String email;
}
