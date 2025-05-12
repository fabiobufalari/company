package com.bufalari.company.converter; // <<< PACOTE CORRIGIDO

import com.bufalari.company.dto.ContactDTO;
import com.bufalari.company.entity.ContactEntity;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter {

    /**
     * Converte ContactEntity (com ID UUID) para ContactDTO (com ID UUID).
     */
    public ContactDTO entityToDTO(ContactEntity contactEntity) {
        if (contactEntity == null) return null;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactEntity.getId()); // <<<--- UUID
        contactDTO.setType(contactEntity.getType());
        contactDTO.setValue(contactEntity.getValue());
        // Não incluir companyDTO aqui para evitar recursão infinita
        return contactDTO;
    }

    /**
     * Converte ContactDTO (com ID UUID) para ContactEntity (com ID UUID).
     * A relação com CompanyEntity deve ser definida no serviço.
     */
    public ContactEntity dtoToEntity(ContactDTO contactDTO) {
        if (contactDTO == null) return null;
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(contactDTO.getId()); // <<<--- UUID (Mantém para atualizações)
        contactEntity.setType(contactDTO.getType());
        contactEntity.setValue(contactDTO.getValue());
        // A referência 'company' NÃO é definida aqui, deve ser feita no CompanyService
        return contactEntity;
    }
}