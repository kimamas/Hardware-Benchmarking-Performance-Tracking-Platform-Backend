package nl.fontys.s3.gamerpc.business;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.converter.GPUConverter;
import nl.fontys.s3.gamerpc.dto.gpu.GPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.gpu.GPUResponse;
import nl.fontys.s3.gamerpc.dto.gpu.GPUUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.GPURepository;
import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GPUService {
    private final GPURepository gpuRepository;


    public List<GPUResponse> getAllGPUs() {
        List<GPUEntity> gpus = gpuRepository.findAll();
        return gpus.stream()
                .map(GPUConverter::fromEntityToSaveResponse)
                .toList();
    }

    public GPUResponse getGPUById(long id) {
        GPUEntity gpu = gpuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GPU not found"));
        return GPUConverter.fromEntityToSaveResponse(gpu);
    }

    public GPUResponse saveGPU(GPUSaveRequest gpu) {
        if(gpuRepository.existsByModel(gpu.getModel())) {
            throw new AlreadyExists("A GPU with this model already exists.");
        }
        GPUEntity savedGPU = gpuRepository.save(GPUConverter.fromSaveRequestToEntity(gpu));
        return GPUConverter.fromEntityToSaveResponse(savedGPU);
    }

    public void deleteGPU(long id) {
        if (!gpuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GPU with ID " + id + " not found");
        }
        gpuRepository.deleteById(id);
    }

    public GPUResponse updateGPU(GPUUpdateRequest gpu) {
        GPUEntity existingGpu = gpuRepository.findById(gpu.getId())
                .orElseThrow(() -> new AlreadyExists("GPU is not found."));

        if (!Objects.equals(existingGpu.getModel(), gpu.getModel()) && gpuRepository.existsByModel(gpu.getModel())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "GPU with this model already exists.");
        }

        GPUEntity updatedGpu = gpuRepository.save(GPUConverter.fromUpdateRequestToEntity(gpu));
        return GPUConverter.fromEntityToSaveResponse(updatedGpu);
    }
}