package com.bufalari.company;

import com.bufalari.company.converter.CompanyConverter; // <<< Pacote Correto
import com.bufalari.company.dto.CompanyDTO;
import com.bufalari.company.entity.CompanyEntity;
import com.bufalari.company.exception.CompanyNotFoundException; // Importar exceção
import com.bufalari.company.repository.CompanyRepository;
import com.bufalari.company.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Para JUnit 5
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension; // Usar extensão Mockito para JUnit 5

import java.time.LocalDate; // Importar LocalDate
import java.util.ArrayList; // Importar ArrayList
import java.util.List; // Importar List
import java.util.Optional;
import java.util.UUID; // <<<--- IMPORT UUID

import static org.junit.jupiter.api.Assertions.*; // Usar assertions JUnit 5
import static org.mockito.ArgumentMatchers.any; // Importar any()
import static org.mockito.Mockito.*; // Importar métodos Mockito

@ExtendWith(MockitoExtension.class) // Habilita a extensão Mockito para JUnit 5
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyConverter companyConverter;

    @InjectMocks // Cria instância de CompanyService e injeta os mocks
    private CompanyService companyService;

    private CompanyDTO companyDTO;
    private CompanyEntity companyEntity;
    private UUID testUUID;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.initMocks(this); // Não é mais necessário com @ExtendWith(MockitoExtension.class)

        testUUID = UUID.randomUUID(); // Gera um UUID para os testes

        // --- Configuração do DTO ---
        companyDTO = new CompanyDTO();
        companyDTO.setId(testUUID); // Usar UUID
        companyDTO.setName("Test Company");
        companyDTO.setBusinessIdentificationNumber("12.345.678/0001-99");
        companyDTO.setMainActivity("Testing");
        companyDTO.setFoundationDate(LocalDate.now().minusYears(1));
        // Adicionar mocks/DTOs para Address, Contacts, Manager, Responsible se necessário para cobrir a lógica
        companyDTO.setAddress(null); // Simplificando, ajuste se necessário
        companyDTO.setContacts(new ArrayList<>());
        companyDTO.setManager(null);
        companyDTO.setResponsible(null);

        // --- Configuração da Entidade ---
        companyEntity = new CompanyEntity();
        companyEntity.setId(testUUID); // Usar UUID
        companyEntity.setName("Test Company");
        companyEntity.setBusinessIdentificationNumber("12.345.678/0001-99");
        companyEntity.setMainActivity("Testing");
        companyEntity.setFoundationDate(LocalDate.now().minusYears(1));
        companyEntity.setAddress(null);
        companyEntity.setContacts(new ArrayList<>());
        companyEntity.setManager(null);
        companyEntity.setResponsible(null);
    }

    @Test
    void testCreateCompany() {
        // Arrange: Define o comportamento dos mocks
        // Quando o converter for chamado com qualquer CompanyDTO, retorna a entidade mock
        when(companyConverter.dtoToEntity(any(CompanyDTO.class))).thenReturn(companyEntity);
        // Quando o repositório salvar qualquer CompanyEntity, retorna a entidade mock (com ID)
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyEntity);
        // Quando o converter for chamado com a entidade salva, retorna o DTO mock
        when(companyConverter.entityToDTO(any(CompanyEntity.class))).thenReturn(companyDTO);

        // Act: Chama o método do serviço a ser testado
        CompanyDTO result = companyService.createCompany(new CompanyDTO()); // Passa um DTO vazio ou preenchido

        // Assert: Verifica se o resultado é o esperado
        assertNotNull(result); // Garante que o resultado não é nulo
        assertEquals(testUUID, result.getId()); // Verifica se o ID retornado é o UUID esperado
        assertEquals("Test Company", result.getName()); // Verifica outros campos se necessário

        // Verifica se os mocks foram chamados como esperado
        verify(companyConverter, times(1)).dtoToEntity(any(CompanyDTO.class));
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
        verify(companyConverter, times(1)).entityToDTO(any(CompanyEntity.class));
    }

    @Test
    void testGetCompanyById_Found() {
        // Arrange
        when(companyRepository.findById(eq(testUUID))).thenReturn(Optional.of(companyEntity)); // Retorna a entidade mock quando encontrar pelo UUID correto
        when(companyConverter.entityToDTO(eq(companyEntity))).thenReturn(companyDTO); // Converte a entidade mock para DTO mock

        // Act
        Optional<CompanyDTO> result = companyService.getCompanyById(testUUID); // Busca pelo UUID

        // Assert
        assertTrue(result.isPresent()); // Verifica se encontrou
        assertEquals(testUUID, result.get().getId()); // Verifica o ID
        assertEquals("Test Company", result.get().getName());

        verify(companyRepository, times(1)).findById(eq(testUUID)); // Verifica a chamada ao repositório
        verify(companyConverter, times(1)).entityToDTO(eq(companyEntity)); // Verifica a chamada ao converter
    }

    @Test
    void testGetCompanyById_NotFound() {
        // Arrange
        UUID nonExistentUUID = UUID.randomUUID();
        when(companyRepository.findById(eq(nonExistentUUID))).thenReturn(Optional.empty()); // Simula não encontrar

        // Act
        Optional<CompanyDTO> result = companyService.getCompanyById(nonExistentUUID);

        // Assert
        assertTrue(result.isEmpty()); // Verifica que o Optional está vazio

        verify(companyRepository, times(1)).findById(eq(nonExistentUUID)); // Verifica a chamada ao repositório
        verify(companyConverter, never()).entityToDTO(any()); // Garante que o converter não foi chamado
    }


    @Test
    void testUpdateCompany_Success() {
        // Arrange
        CompanyDTO dtoToUpdate = new CompanyDTO(); // DTO com dados para atualização
        dtoToUpdate.setName("Updated Test Company");
        // ... preencher outros campos do DTO para update

        CompanyEntity updatedEntity = new CompanyEntity(); // Entidade como seria após converter o DTO de update
        updatedEntity.setId(testUUID); // ID não muda
        updatedEntity.setName("Updated Test Company");
        // ... preencher outros campos da entidade atualizada

        CompanyDTO expectedResultDTO = new CompanyDTO(); // DTO como deve ser retornado após o update
        expectedResultDTO.setId(testUUID);
        expectedResultDTO.setName("Updated Test Company");
        // ... preencher outros campos do DTO resultado

        when(companyRepository.existsById(eq(testUUID))).thenReturn(true); // Simula que a empresa existe
        when(companyConverter.dtoToEntity(any(CompanyDTO.class))).thenReturn(updatedEntity); // Converte DTO de update para entidade
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(updatedEntity); // Salva a entidade atualizada
        when(companyConverter.entityToDTO(eq(updatedEntity))).thenReturn(expectedResultDTO); // Converte entidade atualizada para DTO resultado

        // Act
        CompanyDTO result = companyService.updateCompany(testUUID, dtoToUpdate); // Chama o update com UUID

        // Assert
        assertNotNull(result);
        assertEquals(testUUID, result.getId());
        assertEquals("Updated Test Company", result.getName());

        verify(companyRepository, times(1)).existsById(eq(testUUID));
        verify(companyConverter, times(1)).dtoToEntity(any(CompanyDTO.class));
        verify(companyRepository, times(1)).save(any(CompanyEntity.class)); // Verifica se save foi chamado
        verify(companyConverter, times(1)).entityToDTO(eq(updatedEntity));
    }

    @Test
    void testUpdateCompany_NotFound() {
        // Arrange
        UUID nonExistentUUID = UUID.randomUUID();
        when(companyRepository.existsById(eq(nonExistentUUID))).thenReturn(false); // Simula que a empresa não existe

        // Act & Assert
        // Verifica se a exceção correta é lançada
        assertThrows(CompanyNotFoundException.class, () -> {
            companyService.updateCompany(nonExistentUUID, companyDTO);
        });

        // Garante que save e converters não foram chamados
        verify(companyRepository, times(1)).existsById(eq(nonExistentUUID));
        verify(companyRepository, never()).save(any());
        verify(companyConverter, never()).dtoToEntity(any());
        verify(companyConverter, never()).entityToDTO(any());
    }

    @Test
    void testDeleteCompany_Success() {
        // Arrange
        when(companyRepository.existsById(eq(testUUID))).thenReturn(true); // Simula que existe para deletar
        doNothing().when(companyRepository).deleteById(eq(testUUID)); // Configura o mock para não fazer nada no deleteById

        // Act
        assertDoesNotThrow(() -> companyService.deleteCompany(testUUID)); // Verifica que não lança exceção

        // Assert
        verify(companyRepository, times(1)).existsById(eq(testUUID)); // Verifica a checagem de existência
        verify(companyRepository, times(1)).deleteById(eq(testUUID)); // Verifica a chamada ao deleteById com UUID
    }

    @Test
    void testDeleteCompany_NotFound() {
        // Arrange
        UUID nonExistentUUID = UUID.randomUUID();
        when(companyRepository.existsById(eq(nonExistentUUID))).thenReturn(false); // Simula que não existe

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> {
            companyService.deleteCompany(nonExistentUUID);
        });

        verify(companyRepository, times(1)).existsById(eq(nonExistentUUID));
        verify(companyRepository, never()).deleteById(any()); // Garante que deleteById não foi chamado
    }

    // Adicionar testes para getAllCompanies se houver lógica mais complexa ou paginação
    @Test
    void testGetAllCompanies() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(List.of(companyEntity)); // Retorna uma lista com a entidade mock
        when(companyConverter.entityToDTO(eq(companyEntity))).thenReturn(companyDTO); // Converte a entidade mock

        // Act
        List<CompanyDTO> result = companyService.getAllCompanies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUUID, result.get(0).getId());

        verify(companyRepository, times(1)).findAll();
        verify(companyConverter, times(1)).entityToDTO(eq(companyEntity));
    }
}