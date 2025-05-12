package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity; // Importar Base
import jakarta.persistence.*;
import lombok.*; // Usar Getter/Setter individuais ou @Data
import org.hibernate.annotations.GenericGenerator; // Importar gerador UUID

import java.util.Objects; // Importar Objects
import java.util.UUID; // <<<--- IMPORT UUID

@Entity
@Getter // Ou @Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Define nomes diferentes para as colunas de auditoria herdadas nesta tabela específica
// para evitar conflito se AuditableBaseEntity for usada em várias tabelas.
@AttributeOverrides({
        @AttributeOverride(name = "createdBy", column = @Column(name = "contact_created_by", updatable = false)),
        @AttributeOverride(name = "createdAt", column = @Column(name = "contact_created_at", nullable = false, updatable = false)),
        @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "contact_last_modified_by")),
        @AttributeOverride(name = "lastModifiedAt", column = @Column(name = "contact_last_modified_at", nullable = false))
})
@Table(name = "contacts", indexes = { // Adiciona índice na FK
        @Index(name = "idx_contact_company_id", columnList = "company_id")
})
// @EqualsAndHashCode(callSuper = true) // Cuidado com equals/hashCode em entidades JPA
public class ContactEntity extends AuditableBaseEntity { // <<< Herda auditoria

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id; // <<<--- Alterado para UUID


    // Relação ManyToOne com Company: Muitos contatos pertencem a uma empresa
    // FetchType.LAZY é geralmente recomendado para performance
    // JoinColumn define a coluna de chave estrangeira na tabela 'contacts'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, // Garante que a FK não seja nula
            foreignKey = @ForeignKey(name = "fk_contact_company")) // Nome da constraint FK (opcional)
    private CompanyEntity company; // Mapeia de volta para CompanyEntity

    @Column(nullable = false, length = 50)
    private String type;  // Ex: "TELEFONE_FIXO", "EMAIL_COMERCIAL"

    @Column(nullable = false, length = 255)
    private String value; // Ex: número de telefone ou email

    // --- equals() e hashCode() baseados apenas no ID ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactEntity that = (ContactEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }
}