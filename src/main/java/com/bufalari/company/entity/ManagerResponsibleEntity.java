package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "managers_responsibles")
public class ManagerResponsibleEntity extends AuditableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String position;
    private String phone;
    private String email;
}
