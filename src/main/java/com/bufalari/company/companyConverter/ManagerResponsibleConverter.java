// Substituído pacote 'companyConverter' por 'converter' / Replaced package name 'companyConverter' with 'converter'
package com.bufalari.company.companyConverter;

import com.bufalari.company.dto.ManagerResponsibleDTO;
import com.bufalari.company.entity.ManagerResponsibleEntity;
import org.springframework.stereotype.Component;

@Component
public class ManagerResponsibleConverter {

    public ManagerResponsibleDTO entityToDTO(ManagerResponsibleEntity entity) {
        ManagerResponsibleDTO dto = new ManagerResponsibleDTO();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public ManagerResponsibleEntity dtoToEntity(ManagerResponsibleDTO dto) {
        ManagerResponsibleEntity entity = new ManagerResponsibleEntity();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
