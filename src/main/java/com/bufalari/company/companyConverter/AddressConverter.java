// Substituído pacote 'companyConverter' por 'converter' / Replaced package name 'companyConverter' with 'converter'
package com.bufalari.company.companyConverter;

import com.bufalari.company.dto.AddressDTO;
import com.bufalari.company.entity.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

    public AddressDTO entityToDTO(AddressEntity addressEntity) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(addressEntity.getStreet());
        addressDTO.setNumber(addressEntity.getNumber());
        addressDTO.setComplement(addressEntity.getComplement());
        addressDTO.setNeighbourhood(addressEntity.getNeighbourhood());
        addressDTO.setCity(addressEntity.getCity());
        addressDTO.setProvince(addressEntity.getProvince());
        addressDTO.setPostalCode(addressEntity.getPostalCode());
        // Generate Google Maps link
        addressDTO.setGoogleMapsLink(generateGoogleMapsLink(addressEntity));
        return addressDTO;
    }

    public AddressEntity dtoToEntity(AddressDTO addressDTO) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setNumber(addressDTO.getNumber());
        addressEntity.setComplement(addressDTO.getComplement());
        addressEntity.setNeighbourhood(addressDTO.getNeighbourhood());
        addressEntity.setCity(addressDTO.getCity());
        addressEntity.setProvince(addressDTO.getProvince());
        addressEntity.setPostalCode(addressDTO.getPostalCode());
        return addressEntity;
    }

    public String generateGoogleMapsLink(AddressEntity address) {
        return "https://maps.google.com/?q=" + address.getStreet() + "+" + address.getNumber() + ",+" + address.getCity();
    }
}
