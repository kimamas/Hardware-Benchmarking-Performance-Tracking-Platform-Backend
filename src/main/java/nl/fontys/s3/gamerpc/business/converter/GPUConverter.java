package nl.fontys.s3.gamerpc.business.converter;

import nl.fontys.s3.gamerpc.domain.GPU;
import nl.fontys.s3.gamerpc.dto.gpu.GPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.gpu.GPUResponse;
import nl.fontys.s3.gamerpc.dto.gpu.GPUUpdateRequest;
import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;

public class GPUConverter {
    private GPUConverter() { throw new IllegalStateException("Utility class");}
    public static GPU fromEntityToDomain(GPUEntity entity){
        return GPU.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .clockSpeed(entity.getClockSpeed())
                .turboSpeed(entity.getTurboSpeed())
                .ram(entity.getRam())
                .build();
    }

    public static GPUEntity fromDomainToEntity(GPU gpu){
        return GPUEntity.builder()
                .id((int) gpu.getId())
                .model(gpu.getModel())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPU fromUpdateRequestToDomain(GPUUpdateRequest gpu){
        return GPU.builder()
                .id(gpu.getId())
                .model(gpu.getModel())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPU fromSaveRequestToDomain(GPUSaveRequest gpu){
        return GPU.builder()
                .model(gpu.getModel())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPUResponse fromEntityToSaveResponse(GPUEntity gpu){
        return GPUResponse.builder()
                .id(gpu.getId())
                .model(gpu.getModel())
                .manufacturer(gpu.getManufacturer())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPUEntity fromSaveRequestToEntity(GPUSaveRequest gpu){
        return GPUEntity.builder()
                .model(gpu.getModel())
                .manufacturer(gpu.getManufacturer())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPUResponse fromDomainToSaveResponse(GPU gpu){
        return GPUResponse.builder()
                .id(gpu.getId())
                .model(gpu.getModel())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }

    public static GPUEntity fromUpdateRequestToEntity(GPUUpdateRequest gpu){
        return GPUEntity.builder()
                .id((int) gpu.getId())
                .model(gpu.getModel())
                .clockSpeed(gpu.getClockSpeed())
                .turboSpeed(gpu.getTurboSpeed())
                .ram(gpu.getRam())
                .build();
    }
}
