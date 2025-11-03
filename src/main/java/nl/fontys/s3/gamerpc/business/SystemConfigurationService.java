package nl.fontys.s3.gamerpc.business;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationRequest;
import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationResponse;
import nl.fontys.s3.gamerpc.persistence.iinterface.*;
import nl.fontys.s3.gamerpc.persistence.entity.CPUEntity;
import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;
import nl.fontys.s3.gamerpc.persistence.entity.SystemConfigurationEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static nl.fontys.s3.gamerpc.business.converter.SystemConfigurationConverter.mapEntityToResponse;

@Service
@AllArgsConstructor
public class SystemConfigurationService {
    private SystemConfigurationRepository systemConfigurationRepository;
    private UserRepository userRepository;
    private CPURepository cpuRepository;
    private GPURepository gpuRepository;
    private RAMRepository ramRepository;

    public SystemConfigurationResponse saveConfiguration(@Valid SystemConfigurationRequest configuration, int userId) {
        Optional<SystemConfigurationEntity> existingConfig = systemConfigurationRepository.findByUserId(userId);

        SystemConfigurationEntity entity;
        if (existingConfig.isPresent()) {
            entity = existingConfig.get();
        } else {
            entity = new SystemConfigurationEntity();
            entity.setUser(userRepository.findById((long) userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found")));
        }

        if (configuration.getCpuId() != 0) {
            CPUEntity cpu = cpuRepository.findById((long) configuration.getCpuId())
                    .orElseThrow(() -> new IllegalArgumentException("CPU not found"));
            entity.setCpu(cpu);
        } else {
            entity.setCpu(null);
        }

        if (configuration.getGpuId() != 0) {
            GPUEntity gpu = gpuRepository.findById((long) configuration.getGpuId())
                    .orElseThrow(() -> new IllegalArgumentException("GPU not found"));
            entity.setGpu(gpu);
        } else {
            entity.setGpu(null);
        }

        if (configuration.getRamId() != 0) {
            RAMEntity ram = ramRepository.findById((long) configuration.getRamId())
                    .orElseThrow(() -> new IllegalArgumentException("RAM not found"));
            entity.setRam(ram);
        } else {
            entity.setRam(null);
        }

        SystemConfigurationEntity savedEntity = systemConfigurationRepository.save(entity);

        return mapEntityToResponse(savedEntity);
    }

    public SystemConfigurationResponse getConfigurationById(Long userId) {
        SystemConfigurationEntity entity = systemConfigurationRepository.findByUserId(Math.toIntExact(userId))
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found for user ID " + userId));

        return mapEntityToResponse(entity);
    }
}
