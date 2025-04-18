// Substituído pacote 'companyConverter' por 'converter' / Replaced package name 'companyConverter' with 'converter'
package com.bufalari.company.companyConverter;

import com.bufalari.company.dto.ContactDTO;
import com.bufalari.company.entity.ContactEntity;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter {

    public ContactDTO entityToDTO(ContactEntity contactEntity) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setType(contactEntity.getType());
        contactDTO.setValue(contactEntity.getValue());
        return contactDTO;
    }

    public ContactEntity dtoToEntity(ContactDTO contactDTO) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setType(contactDTO.getType());
        contactEntity.setValue(contactDTO.getValue());
        return contactEntity;
    }
}
