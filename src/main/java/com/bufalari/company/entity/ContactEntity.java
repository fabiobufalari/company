package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@AttributeOverrides({ // <<< ADICIONAR OVERRIDES
        @AttributeOverride(name = "createdBy", column = @Column(name = "created_by", updatable = false)),
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false, updatable = false)),
        @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "last_modified_by")),
        @AttributeOverride(name = "lastModifiedAt", column = @Column(name = "last_modified_at", nullable = false))
})
@Table(name = "contacts")
public class ContactEntity extends AuditableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY) // Boa prática usar LAZY
    @JoinColumn(name = "company_id", nullable = false) // Garante que a FK não seja nula
    private CompanyEntity company; // Deve mapear de volta para CompanyEntity

    private String type;  // E.g., "phone", "email"
    private String value; // E.g., phone number or email
}
