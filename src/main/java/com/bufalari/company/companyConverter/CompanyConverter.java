// Substituído pacote 'companyConverter' por 'converter' / Replaced package name 'companyConverter' with 'converter'
package com.bufalari.company.companyConverter;

import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.entity.ContactEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyConverter {
    // Substituído injeção de dependência via @Autowired em campos por injeção via construtor / Replaced field injection with constructor-based dependency injection
    private final AddressConverter addressConverter;
    private final ContactConverter contactConverter;
    private final ManagerResponsibleConverter managerResponsibleConverter;

    // Construtor para injeção de dependências / Constructor for dependency injection
    public CompanyConverter(AddressConverter addressConverter, ContactConverter contactConverter, ManagerResponsibleConverter managerResponsibleConverter) {
        this.addressConverter = addressConverter;
        this.contactConverter = contactConverter;
        this.managerResponsibleConverter = managerResponsibleConverter;
    }

    public CompanyDTO entityToDTO(CompanyEntity companyEntity) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(companyEntity.getId());
        companyDTO.setName(companyEntity.getName());
        companyDTO.setBusinessIdentificationNumber(companyEntity.getBusinessIdentificationNumber());
        // Convert AddressEntity to AddressDTO
        companyDTO.setAddress(addressConverter.entityToDTO(companyEntity.getAddress()));
        // Convert List<ContactEntity> to List<ContactDTO>
        List<ContactEntity> contactEntities = companyEntity.getContacts();
        companyDTO.setContacts(contactEntities.stream()
                .map(contactConverter::entityToDTO)
                .collect(Collectors.toList()));
        companyDTO.setMainActivity(companyEntity.getMainActivity());
        companyDTO.setFoundationDate(companyEntity.getFoundationDate());
        // Convert ManagerResponsibleEntity to ManagerResponsibleDTO
        companyDTO.setManager(managerResponsibleConverter.entityToDTO(companyEntity.getManager()));
        companyDTO.setResponsible(managerResponsibleConverter.entityToDTO(companyEntity.getResponsible()));
        // Generate Google Maps link
        companyDTO.setGoogleMapsLink(addressConverter.generateGoogleMapsLink(companyEntity.getAddress()));
        return companyDTO;
    }

    public CompanyEntity dtoToEntity(CompanyDTO companyDTO) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setId(companyDTO.getId());
        companyEntity.setName(companyDTO.getName());
        companyEntity.setBusinessIdentificationNumber(companyDTO.getBusinessIdentificationNumber());
        // Convert AddressDTO to AddressEntity
        companyEntity.setAddress(addressConverter.dtoToEntity(companyDTO.getAddress()));
        // Convert List<ContactDTO> to List<ContactEntity>
        companyEntity.setContacts(companyDTO.getContacts().stream()
                .map(contactConverter::dtoToEntity)
                .collect(Collectors.toList()));
        companyEntity.setMainActivity(companyDTO.getMainActivity());
        companyEntity.setFoundationDate(companyDTO.getFoundationDate());
        // Convert ManagerResponsibleDTO to ManagerResponsibleEntity
        companyEntity.setManager(managerResponsibleConverter.dtoToEntity(companyDTO.getManager()));
        companyEntity.setResponsible(managerResponsibleConverter.dtoToEntity(companyDTO.getResponsible()));
        return companyEntity;
    }
}
