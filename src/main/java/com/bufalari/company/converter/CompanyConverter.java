package com.bufalari.company.converter; // <<< PACOTE CORRIGIDO

import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.entity.ContactEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyConverter {
    // Injeção via construtor é preferível
    private final AddressConverter addressConverter;
    private final ContactConverter contactConverter;
    private final ManagerResponsibleConverter managerResponsibleConverter;

    // Construtor para injeção de dependências
    public CompanyConverter(AddressConverter addressConverter, ContactConverter contactConverter, ManagerResponsibleConverter managerResponsibleConverter) {
        this.addressConverter = addressConverter;
        this.contactConverter = contactConverter;
        this.managerResponsibleConverter = managerResponsibleConverter;
    }

    /**
     * Converte CompanyEntity (com ID UUID) para CompanyDTO (com ID UUID).
     */
    public CompanyDTO entityToDTO(CompanyEntity companyEntity) {
        if (companyEntity == null) return null;

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(companyEntity.getId()); // <<<--- UUID
        companyDTO.setName(companyEntity.getName());
        companyDTO.setBusinessIdentificationNumber(companyEntity.getBusinessIdentificationNumber());

        // Converte AddressEntity para AddressDTO
        companyDTO.setAddress(addressConverter.entityToDTO(companyEntity.getAddress()));

        // Converte List<ContactEntity> para List<ContactDTO> (ContactDTO agora tem ID)
        List<ContactEntity> contactEntities = companyEntity.getContacts();
        if (contactEntities != null) {
            companyDTO.setContacts(contactEntities.stream()
                    .map(contactConverter::entityToDTO) // Usa o conversor de contato
                    .collect(Collectors.toList()));
        }

        companyDTO.setMainActivity(companyEntity.getMainActivity());
        companyDTO.setFoundationDate(companyEntity.getFoundationDate());

        // Converte ManagerResponsibleEntity para ManagerResponsibleDTO (ManagerResponsibleDTO agora tem ID)
        companyDTO.setManager(managerResponsibleConverter.entityToDTO(companyEntity.getManager()));
        companyDTO.setResponsible(managerResponsibleConverter.entityToDTO(companyEntity.getResponsible()));

        // Gera link do Google Maps (se o endereço não for nulo)
        if (companyEntity.getAddress() != null) {
            companyDTO.setGoogleMapsLink(addressConverter.generateGoogleMapsLink(companyEntity.getAddress()));
        }

        return companyDTO;
    }

    /**
     * Converte CompanyDTO (com ID UUID) para CompanyEntity (com ID UUID).
     */
    public CompanyEntity dtoToEntity(CompanyDTO companyDTO) {
        if (companyDTO == null) return null;

        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(companyDTO.getId()); // <<<--- UUID (Mantém para atualizações)
        companyEntity.setName(companyDTO.getName());
        companyEntity.setBusinessIdentificationNumber(companyDTO.getBusinessIdentificationNumber());

        // Converte AddressDTO para AddressEntity
        companyEntity.setAddress(addressConverter.dtoToEntity(companyDTO.getAddress()));

        // Converte List<ContactDTO> para List<ContactEntity>
        if (companyDTO.getContacts() != null) {
            List<ContactEntity> contactEntities = companyDTO.getContacts().stream()
                    .map(contactConverter::dtoToEntity) // Usa o conversor de contato
                    .collect(Collectors.toList());
            // Define a referência bidirecional
            contactEntities.forEach(contact -> contact.setCompany(companyEntity));
            companyEntity.setContacts(contactEntities);
        }

        companyEntity.setMainActivity(companyDTO.getMainActivity());
        companyEntity.setFoundationDate(companyDTO.getFoundationDate());

        // Converte ManagerResponsibleDTO para ManagerResponsibleEntity
        companyEntity.setManager(managerResponsibleConverter.dtoToEntity(companyDTO.getManager()));
        companyEntity.setResponsible(managerResponsibleConverter.dtoToEntity(companyDTO.getResponsible()));

        return companyEntity;
    }
}