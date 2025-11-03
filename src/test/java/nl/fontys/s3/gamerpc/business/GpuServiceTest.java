package nl.fontys.s3.gamerpc.business;

import nl.fontys.s3.gamerpc.dto.gpu.GPUResponse;
import nl.fontys.s3.gamerpc.dto.gpu.GPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.gpu.GPUUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.GPURepository;
import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
@SpringBootTest
class GpuServiceTest {

    @Mock
    private GPURepository gpuRepository;

    @InjectMocks
    private GPUService gpuService;

    private GPUEntity mockGPUEntity;
    private GPUSaveRequest mockSaveRequest;
    private GPUUpdateRequest mockUpdateRequest;
    private GPUResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockGPUEntity = new GPUEntity(1, "Model X", "NVIDIA", 12, 1500, 1750);
        mockSaveRequest = new GPUSaveRequest("Model X", "NVIDIA", 12, 1500, 1750);
        mockUpdateRequest = new GPUUpdateRequest(1, "Model Y", "NVIDIA", 12, 1500,1750);
        mockResponse = new GPUResponse(1, "Model X", "NVIDIA", 12, 1500,1750);
    }

    @Test
    void getAllGPUs_ReturnsListOfGPUs() {
        GPUEntity gpu2 = new GPUEntity(2, "Model Y", "NVIDIA", 12, 1500, 1750);
        when(gpuRepository.findAll()).thenReturn(List.of(mockGPUEntity, gpu2));

        List<GPUResponse> gpus = gpuService.getAllGPUs();

        assertEquals(2, gpus.size());
        assertEquals("Model X", gpus.get(0).getModel());
        assertEquals("Model Y", gpus.get(1).getModel());
    }

    @Test
    void getGPUById_ExistingId_ReturnsGPU() {
        when(gpuRepository.findById(1L)).thenReturn(Optional.of(mockGPUEntity));

        GPUResponse result = gpuService.getGPUById(1L);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    void getGPUById_NonExistingId_ReturnsEmptyOptional() {
        when(gpuRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                gpuService.getGPUById(1L)
        );
        assertEquals("GPU not found", exception.getReason());
    }

    @Test
    void saveGPU_ValidInput_SuccessfullySavesGPU() {
        when(gpuRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(false);
        when(gpuRepository.save(any(GPUEntity.class))).thenReturn(mockGPUEntity);

        GPUResponse response = gpuService.saveGPU(mockSaveRequest);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void saveGPU_DuplicateModel_ThrowsAlreadyExistsException() {
        when(gpuRepository.existsByModel(mockSaveRequest.getModel())).thenReturn(true);

        AlreadyExists exception = assertThrows(AlreadyExists.class, () ->
                gpuService.saveGPU(mockSaveRequest)
        );
        assertEquals("A GPU with this model already exists.", exception.getReason());
    }

    @Test
    void deleteGPU_ExistingId_DeletesGPU() {
        when(gpuRepository.existsById(1L)).thenReturn(true);

        gpuService.deleteGPU(1L);

        verify(gpuRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGPU_NonExistingId_ThrowsException() {
        when(gpuRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                gpuService.deleteGPU(1L)
        );
        assertEquals("GPU with ID 1 not found", exception.getReason());
    }

    @Test
    void updateGPU_ValidInput_SuccessfullyUpdatesGPU() {
        when(gpuRepository.findById(1L)).thenReturn(Optional.ofNullable(mockGPUEntity));
        when(gpuRepository.existsByModel(Mockito.any())).thenReturn(false);
        when(gpuRepository.save(any(GPUEntity.class))).thenReturn(mockGPUEntity);

        GPUResponse response = gpuService.updateGPU(mockUpdateRequest);

        assertThat(mockResponse)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    @Test
    void updateGPU_NonExistingId_ThrowsException() {
        when(gpuRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                gpuService.updateGPU(mockUpdateRequest)
        );

        assertEquals("GPU is not found.", exception.getReason());
    }

    @Test
    void updateGPU_ThrowsError_ThisModelAlreadyExists() {
        when(gpuRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(mockGPUEntity));
        when(gpuRepository.existsByModel(Mockito.any())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                gpuService.updateGPU(mockUpdateRequest)
        );

        assertEquals("GPU with this model already exists.", exception.getReason());
    }
}