package com.bufalari.company.service;

import com.bufalari.company.converter.CompanyConverter; // << Pacote Correto
import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.exception.CompanyNotFoundException;
import com.bufalari.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor; // Usar Lombok para injeção
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional

import java.util.List;
import java.util.Optional;
import java.util.UUID; // <<<--- IMPORT UUID
import java.util.stream.Collectors; // Importar Collectors

@Service
@RequiredArgsConstructor // Injeção via construtor pelo Lombok
@Transactional // Define transacionalidade padrão para os métodos públicos
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;
    private final CompanyConverter companyConverter;

    /**
     * Cria uma nova empresa.
     * @param companyDTO Dados da empresa a ser criada.
     * @return DTO da empresa criada com o ID gerado.
     */
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        log.info("Attempting to create company: {}", companyDTO.getName());
        if (companyDTO.getId() != null) {
            log.warn("ID provided for company creation will be ignored.");
            companyDTO.setId(null); // Garante que o ID seja gerado pelo banco
        }
        CompanyEntity companyEntity = companyConverter.dtoToEntity(companyDTO);
        // Relações bidirecionais (como Contact->Company) são definidas no converter ou aqui antes de salvar
        // Se não foram definidas no converter:
        // if (companyEntity.getContacts() != null) {
        //     companyEntity.getContacts().forEach(contact -> contact.setCompany(companyEntity));
        // }
        CompanyEntity savedCompany = companyRepository.save(companyEntity);
        log.info("Company created successfully with ID: {}", savedCompany.getId());
        return companyConverter.entityToDTO(savedCompany);
    }

    /**
     * Busca uma empresa pelo seu UUID.
     * @param id O UUID da empresa.
     * @return Um Optional contendo o CompanyDTO se encontrado, senão Optional vazio.
     */
    @Transactional(readOnly = true) // Otimização para leitura
    public Optional<CompanyDTO> getCompanyById(UUID id) { // <<<--- UUID
        log.debug("Attempting to find company by ID: {}", id);
        Optional<CompanyDTO> companyOpt = companyRepository.findById(id) // <<<--- findById com UUID
                .map(companyConverter::entityToDTO); // Converte a entidade para DTO se presente
        if (companyOpt.isEmpty()) {
            log.warn("Company with ID {} not found.", id);
        }
        return companyOpt;
    }

    /**
     * Retorna uma lista de todas as empresas.
     * @return Lista de CompanyDTOs.
     */
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        log.debug("Fetching all companies.");
        List<CompanyEntity> companies = companyRepository.findAll();
        log.info("Found {} companies.", companies.size());
        return companies.stream()
                .map(companyConverter::entityToDTO)
                .collect(Collectors.toList()); // Usa Collectors.toList()
    }

    /**
     * Atualiza uma empresa existente.
     * @param id O UUID da empresa a ser atualizada.
     * @param companyDTO DTO com os dados atualizados.
     * @return O CompanyDTO atualizado.
     * @throws CompanyNotFoundException Se a empresa com o ID fornecido não existir.
     */
    public CompanyDTO updateCompany(UUID id, CompanyDTO companyDTO) { // <<<--- UUID
        log.info("Attempting to update company with ID: {}", id);
        // Verifica se a empresa existe antes de tentar atualizar
        if (!companyRepository.existsById(id)) { // <<<--- existsById com UUID
            log.warn("Update failed. Company with ID {} not found.", id);
            throw new CompanyNotFoundException(id); // Lança exceção específica
        }
        CompanyEntity companyEntity = companyConverter.dtoToEntity(companyDTO);
        companyEntity.setId(id); // Define o ID da entidade a ser atualizada

        // Garante consistência bidirecional ao atualizar
        // if (companyEntity.getContacts() != null) {
        //     companyEntity.getContacts().forEach(contact -> contact.setCompany(companyEntity));
        // }

        CompanyEntity updatedCompany = companyRepository.save(companyEntity);
        log.info("Company with ID {} updated successfully.", id);
        return companyConverter.entityToDTO(updatedCompany);
    }

    /**
     * Deleta uma empresa pelo seu UUID.
     * @param id O UUID da empresa a ser deletada.
     * @throws CompanyNotFoundException Se a empresa com o ID fornecido não existir.
     */
    public void deleteCompany(UUID id) { // <<<--- UUID
        log.info("Attempting to delete company with ID: {}", id);
        if (!companyRepository.existsById(id)) { // <<<--- existsById com UUID
            log.warn("Delete failed. Company with ID {} not found.", id);
            throw new CompanyNotFoundException(id); // Lança exceção específica
        }
        // Considerar lógica de validação antes de deletar (ex: verificar se há projetos associados)
        companyRepository.deleteById(id); // <<<--- deleteById com UUID
        log.info("Company with ID {} deleted successfully.", id);
    }
}