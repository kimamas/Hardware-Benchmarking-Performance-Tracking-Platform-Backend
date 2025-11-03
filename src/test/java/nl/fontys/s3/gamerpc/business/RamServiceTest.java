package nl.fontys.s3.gamerpc.business;

import nl.fontys.s3.gamerpc.dto.ram.RAMResponse;
import nl.fontys.s3.gamerpc.dto.ram.RAMSaveRequest;
import nl.fontys.s3.gamerpc.dto.ram.RAMUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.RAMRepository;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class RamServiceTest {

    @Mock
    private RAMRepository ramRepository;

    @InjectMocks
    private RAMService ramService;

    private RAMEntity mockRAMEntity;
    private RAMSaveRequest mockSaveRequest;
    private RAMUpdateRequest mockUpdateRequest;
    private RAMResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockRAMEntity = new RAMEntity(1, "Model A", "Crucial", 16, 3200, "DDR5", "CL16", 2);
        mockSaveRequest = new RAMSaveRequest("Model A", "Crucial", 16, 3200, "DDR5", "CL16", 2);
        mockUpdateRequest = new RAMUpdateRequest(1, "Model A", "Crucial", 16, 3200, "DDR5", "CL16", 2);
        mockResponse = new RAMResponse(1, "Model A", "Crucial", 16, 3200, "DDR5", "CL16", 2);
    }

    @Test
    void getAllRAMs_ReturnsListOfRAMs() {
        RAMEntity ram2 = new RAMEntity(2, "Model B", "Crucial", 16, 3200, "DDR5", "CL16", 2);
        when(ramRepository.findAll()).thenReturn(List.of(mockRAMEntity, ram2));

        List<RAMEntity> rams = ramService.getAllRAMs();

        assertEquals(2, rams.size());
        assertEquals("Model A", rams.get(0).getModel());
        assertEquals("Model B", rams.get(1).getModel());
    }

    @Test
    void getRAMById_ExistingId_ReturnsRAM() {
        when(ramRepository.findById(1L)).thenReturn(Optional.of(mockRAMEntity));

        RAMResponse result = ramService.getRAMById(1L);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    void getRAMById_NonExistingId_ReturnsEmptyOptional() {
        when(ramRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ramService.getRAMById(1L)
        );
        assertEquals("RAM not found", exception.getReason());
    }

    @Test
    void saveRAM_ValidInput_SuccessfullySavesRAM() {
        when(ramRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(false);
        when(ramRepository.save(any(RAMEntity.class))).thenReturn(mockRAMEntity);

        RAMResponse response = ramService.saveRAM(mockSaveRequest);

        assertEquals(1L, response.getId());
        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void saveRAM_DuplicateModel_ThrowsAlreadyExistsException() {
        when(ramRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(true);

        ResponseStatusException exception = assertThrows(AlreadyExists.class, () ->
                ramService.saveRAM(mockSaveRequest)
        );
        assertEquals("A RAM with this model already exists.", exception.getReason());
    }

    @Test
    void deleteRAM_ExistingId_DeletesRAM() {
        when(ramRepository.existsById(1L)).thenReturn(true);

        ramService.deleteRAM(1L);

        verify(ramRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRAM_NonExistingId_ThrowsException() {
        when(ramRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ramService.deleteRAM(1L)
        );
        assertEquals("RAM with ID 1 not found", exception.getReason());
    }

    @Test
    void updateRAM_ValidInput_SuccessfullyUpdatesRAM() {
        when(ramRepository.findById(1L)).thenReturn(Optional.ofNullable(mockRAMEntity));
        when(ramRepository.save(any(RAMEntity.class))).thenReturn(mockRAMEntity);

        RAMResponse response = ramService.updateRAM(mockUpdateRequest);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void updateRAM_NonExistingId_ThrowsException() {
        when(ramRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ramService.updateRAM(mockUpdateRequest)
        );

        assertEquals("Ram is not found.", exception.getReason());
    }
    @Test
    void updateRAM_ThrowsError_ThisModelAlreadyExists() {
        when(ramRepository.findById(1L)).thenReturn(Optional.ofNullable(mockRAMEntity));
        when(ramRepository.existsByModel(Mockito.any())).thenReturn(true);
        RAMUpdateRequest updateRequest = new RAMUpdateRequest(1, "Model B", "Crucial", 16, 3200, "DDR5", "CL16", 2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                ramService.updateRAM(updateRequest)
        );

        assertEquals("RAM with this model already exists.", exception.getReason());
    }
}
