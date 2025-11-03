package nl.fontys.s3.gamerpc.business;

import nl.fontys.s3.gamerpc.dto.cpu.CPUResponse;
import nl.fontys.s3.gamerpc.dto.cpu.CPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.cpu.CPUUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.CPURepository;
import nl.fontys.s3.gamerpc.persistence.entity.CPUEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CpuServiceTest {

    @Mock
    private CPURepository cpuRepository;

    @InjectMocks
    private CPUService cpuService;

    private CPUEntity mockCPUEntity;
    private CPUSaveRequest mockSaveRequest;
    private CPUUpdateRequest mockUpdateRequest;
    private CPUResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockCPUEntity = new CPUEntity(1, "I7 12500k", "Intel", 12, 2.5, 3.1);
        mockSaveRequest =  new CPUSaveRequest("I7 12500k", "Intel", 12, 2.5, 3.1);
        mockUpdateRequest = new CPUUpdateRequest(1L, "UpdatedManufacturer", "UpdatedModel", 14, 2.5, 3.1);
        mockResponse = new CPUResponse(1L, "I7 12500k", "Intel", 12, 2.5, 3.1);
    }

    @Test
    void getAllCPUs_ReturnsListOfCPUs() {
        CPUEntity cpu2 = new CPUEntity(2, "I5 12500k", "Intel", 12, 3.0, 4);
        when(cpuRepository.findAll()).thenReturn(List.of(mockCPUEntity, cpu2));

        List<CPUResponse> result = cpuService.getAllCPUs();

        assertEquals(2, result.size());
        assertEquals("I7 12500k", result.get(0).getModel());
        assertEquals("I5 12500k", result.get(1).getModel());
    }

    @Test
    void getCPUById_ExistingId_ReturnsCPU() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(mockCPUEntity));

        CPUResponse result = cpuService.getCPUById(1L);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    void getCPUById_NonExistingId_ReturnsEmptyOptional() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                cpuService.getCPUById(1L)
        );
        assertEquals("CPU not found", exception.getReason());
    }

    @Test
    void saveCPU_ValidInput_SuccessfullySavesCPU() {
        when(cpuRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(false);
        when(cpuRepository.save(any(CPUEntity.class))).thenReturn(mockCPUEntity);

        CPUResponse response = cpuService.saveCPU(mockSaveRequest);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void saveCPU_DuplicateModel_ThrowsAlreadyExistsException() {
        when(cpuRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(true);

        AlreadyExists exception = assertThrows(AlreadyExists.class, () ->
                cpuService.saveCPU(mockSaveRequest)
        );
        assertEquals("A CPU with this model already exists.", exception.getReason());
    }

    @Test
    void updateCPU_ValidInput_SuccessfullyUpdatesCPU() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(mockCPUEntity));
        when(cpuRepository.existsByModel(mockUpdateRequest.getModel())).thenReturn(false);
        when(cpuRepository.save(any(CPUEntity.class))).thenReturn(mockCPUEntity);

        CPUResponse response = cpuService.updateCPU(mockUpdateRequest);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void updateCPU_NonExistingId_ThrowsException() {
        when(cpuRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                cpuService.updateCPU(mockUpdateRequest)
        );

        assertEquals("CPU is not found.", exception.getReason());
    }

    @Test
    void updateCPU_DuplicateModel_ThrowsException() {
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(mockCPUEntity));
        when(cpuRepository.existsByModel(mockUpdateRequest.getModel())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                cpuService.updateCPU(mockUpdateRequest)
        );

        assertEquals("CPU with this model already exists.", exception.getReason());
    }

    @Test
    void deleteCPU_ExistingId_DeletesCPU() {
        when(cpuRepository.existsById(1L)).thenReturn(true);

        cpuService.deleteCPU(1L);

        verify(cpuRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCPU_NonExistingId_ThrowsException() {
        when(cpuRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                cpuService.deleteCPU(1L)
        );
        assertEquals("CPU with ID 1 not found", exception.getReason());
    }
}
