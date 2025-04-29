package com.bufalari.company.entity;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor; // Adicione se não existir
import lombok.AllArgsConstructor; // Adicione se não existir

@Embeddable
@Data
@NoArgsConstructor // Adicionar
@AllArgsConstructor // Adicionar
public class AddressEntity { // <<< NÃO DEVE ESTENDER AuditableBaseEntity

    private String street;
    private String number;
    private String complement;
    private String neighbourhood;
    private String city;
    private String province;
    private String postalCode;

    // Remover quaisquer campos de auditoria que possam ter sido copiados aqui
}