package nl.fontys.s3.gamerpc.business.converter;

import nl.fontys.s3.gamerpc.dto.ram.RAMSaveRequest;
import nl.fontys.s3.gamerpc.dto.ram.RAMResponse;
import nl.fontys.s3.gamerpc.dto.ram.RAMUpdateRequest;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;

public class RAMConverter {
    private RAMConverter() { throw new IllegalStateException("Utility class");}
    public static RAMEntity fromSaveRequestToEntity(RAMSaveRequest request) {
        return RAMEntity.builder()
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .ram(request.getRam())
                .memorySpeed(request.getMemorySpeed())
                .type(request.getType())
                .timings(request.getTimings())
                .channels(request.getChannels())
                .build();
    }

    public static RAMResponse fromEntityToResponse(RAMEntity entity) {
        return RAMResponse.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .manufacturer(entity.getManufacturer())
                .ram(entity.getRam())
                .memorySpeed(entity.getMemorySpeed())
                .type(entity.getType())
                .timings(entity.getTimings())
                .channels(entity.getChannels())
                .build();
    }

    public static RAMEntity fromUpdateRequestToEntity(RAMUpdateRequest request) {
        return RAMEntity.builder()
                .id((int) request.getId())
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .ram(request.getRam())
                .memorySpeed(request.getMemorySpeed())
                .type(request.getType())
                .timings(request.getTimings())
                .channels(request.getChannels())
                .build();
    }
}
