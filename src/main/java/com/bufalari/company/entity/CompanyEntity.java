package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity; // Importar
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode; // Importar
import lombok.NoArgsConstructor; // Adicionar se não existir
import lombok.AllArgsConstructor; // Adicionar se não existir
import lombok.Builder; // Adicionar se usar builder

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true) // Adicionar
@NoArgsConstructor // Adicionar
@AllArgsConstructor // Adicionar
@Builder // Adicionar (opcional)
@Table(name = "companies")
public class CompanyEntity extends AuditableBaseEntity { // <<< VERIFICAR EXTENDS

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

    // **** CERTIFIQUE-SE DE QUE NÃO HÁ CAMPOS DE AUDITORIA AQUI ****
    // private LocalDateTime createdAt; // << REMOVER SE EXISTIR
    // private String createdBy;       // << REMOVER SE EXISTIR
    // ... etc ...
}