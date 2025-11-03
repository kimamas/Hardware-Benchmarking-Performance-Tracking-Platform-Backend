package nl.fontys.s3.gamerpc.business;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.converter.CPUConverter;
import nl.fontys.s3.gamerpc.dto.cpu.CPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.cpu.CPUResponse;
import nl.fontys.s3.gamerpc.dto.cpu.CPUUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.CPURepository;
import nl.fontys.s3.gamerpc.persistence.entity.CPUEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CPUService {
    private final CPURepository cpuRepository;


    public List<CPUResponse> getAllCPUs() {
        List<CPUEntity> cpus = cpuRepository.findAll().stream().toList();
        return cpus.stream()
                .map(CPUConverter::fromEntityToSaveResponse)
                .toList();
    }

    public CPUResponse getCPUById(long id) {
        CPUEntity cpu = cpuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CPU not found"));
        return CPUConverter.fromEntityToSaveResponse(cpu);
    }

    public CPUResponse saveCPU(CPUSaveRequest cpu) {
        if(cpuRepository.existsByModel(cpu.getModel())) {
            throw new AlreadyExists("A CPU with this model already exists.");
        }
        CPUEntity savedCPU = cpuRepository.save(CPUConverter.fromSaveRequestToEntity(cpu));
        return CPUConverter.fromEntityToSaveResponse(savedCPU);
    }

    public void deleteCPU(long id) {
        if (!cpuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPU with ID " + id + " not found");
        }
        cpuRepository.deleteById(id);
    }
    public CPUResponse updateCPU(CPUUpdateRequest cpu) {
        CPUEntity existingCpu = cpuRepository.findById(cpu.getId())
                .orElseThrow(() -> new AlreadyExists("CPU is not found."));

        if (!Objects.equals(existingCpu.getModel(), cpu.getModel()) && cpuRepository.existsByModel(cpu.getModel())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPU with this model already exists.");
        }

        CPUEntity updatedCpu = cpuRepository.save(CPUConverter.fromUpdateRequestToEntity(cpu));
        return CPUConverter.fromEntityToSaveResponse(updatedCpu);
    }
}
