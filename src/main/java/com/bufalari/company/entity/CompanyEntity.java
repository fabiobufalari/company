package com.bufalari.company.entity;

import com.bufalari.company.auditing.AuditableBaseEntity; // Importar
import jakarta.persistence.*;
import lombok.*; // Importar todas as anotações Lombok necessárias
import org.hibernate.annotations.GenericGenerator; // Importar gerador UUID

import java.time.LocalDate;
import java.util.ArrayList; // Importar ArrayList
import java.util.List;
import java.util.Objects; // Importar Objects
import java.util.UUID; // <<<--- IMPORT UUID

@Entity
@Getter // Usar Getter/Setter individuais ou @Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Adicionado para facilitar criação em testes/serviços
@Table(name = "companies") // Nome da tabela no plural é convenção comum
// @EqualsAndHashCode(callSuper = true) // Cuidado com Equals/HashCode em entidades JPA, especialmente com relações LAZY. Baseado no ID é mais seguro.
public class CompanyEntity extends AuditableBaseEntity { // <<< Garante herança

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator" // Gerador padrão de UUID
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid") // Define coluna como UUID no DB
    private UUID id; // <<<--- Alterado para UUID

    @Column(nullable = false, length = 255) // Adicionar restrições se necessário
    private String name;

    @Column(nullable = false, length = 20, unique = true) // Exemplo: CNPJ/EIN é único
    private String businessIdentificationNumber;

    @Embedded // Endereço embutido na tabela 'companies'
    private AddressEntity address;

    // Relação OneToMany com Contact: Uma empresa tem muitos contatos
    // cascade = CascadeType.ALL: Operações na Company (persist, merge, remove) são cascateadas para Contact
    // orphanRemoval = true: Se um Contact é removido da lista 'contacts', ele é deletado do banco
    // fetch = FetchType.LAZY: Contatos são carregados apenas quando acessados explicitamente
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // Inicializa a lista para evitar NullPointerException
    private List<ContactEntity> contacts = new ArrayList<>();

    @Column(nullable = false, length = 255)
    private String mainActivity;

    @Column(nullable = false)
    private LocalDate foundationDate;

    // Relação OneToOne com ManagerResponsible (para o responsável)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id", // FK na tabela 'companies'
            foreignKey = @ForeignKey(name = "fk_company_responsible")) // Nome da constraint FK (opcional)
    private ManagerResponsibleEntity responsible; // Responsável Legal

    // Relação OneToOne com ManagerResponsible (para o gerente)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "manager_id", referencedColumnName = "id", // FK na tabela 'companies'
            foreignKey = @ForeignKey(name = "fk_company_manager")) // Nome da constraint FK (opcional)
    private ManagerResponsibleEntity manager; // Gerente

    // --- Métodos auxiliares para gerenciar relações bidirecionais (boa prática) ---
    public void addContact(ContactEntity contact) {
        if (contact != null) {
            if (this.contacts == null) {
                this.contacts = new ArrayList<>();
            }
            this.contacts.add(contact);
            contact.setCompany(this); // Mantém a consistência bidirecional
        }
    }

    public void removeContact(ContactEntity contact) {
        if (contact != null && this.contacts != null) {
            this.contacts.remove(contact);
            contact.setCompany(null); // Remove a referência
        }
    }

    // --- equals() e hashCode() baseados apenas no ID (mais seguro para JPA) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity that = (CompanyEntity) o;
        // Só compara pelo ID se ambos não forem nulos
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // Usa 0 ou um valor fixo se o ID for nulo, ou o hash do ID se não for nulo
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    // **** CERTIFIQUE-SE DE QUE NÃO HÁ CAMPOS DE AUDITORIA DUPLICADOS AQUI ****
    // Os campos de auditoria são herdados de AuditableBaseEntity
}