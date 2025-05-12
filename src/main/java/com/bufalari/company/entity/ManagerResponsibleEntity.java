package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity; // Importar Base
import jakarta.persistence.*;
import lombok.*; // Usar Getter/Setter ou @Data
import org.hibernate.annotations.GenericGenerator; // Importar gerador UUID

import java.util.Objects; // Importar Objects
import java.util.UUID; // <<<--- IMPORT UUID

@Entity
@Getter // Ou @Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Opcional
@Table(name = "managers_responsibles") // Nome da tabela
// @EqualsAndHashCode(callSuper = true) // Cuidado com equals/hashCode
public class ManagerResponsibleEntity extends AuditableBaseEntity { // <<< Herda auditoria

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id; // <<<--- Alterado para UUID

    @Column(length = 50, unique = true) // Código pode ser único
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String position; // Cargo

    @Column(length = 50)
    private String phone;

    @Column(length = 255, unique = true) // Email geralmente é único
    private String email;

    // Relacionamentos com CompanyEntity (se necessário mapear aqui, mas já está mapeado em CompanyEntity com @OneToOne)
    // @OneToOne(mappedBy = "responsible")
    // private CompanyEntity companyResponsibleFor;
    //
    // @OneToOne(mappedBy = "manager")
    // private CompanyEntity companyManagedBy;

    // --- equals() e hashCode() baseados apenas no ID ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerResponsibleEntity that = (ManagerResponsibleEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}