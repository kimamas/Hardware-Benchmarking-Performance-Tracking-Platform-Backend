package nl.fontys.s3.gamerpc.business.converter;

import nl.fontys.s3.gamerpc.domain.CPU;
import nl.fontys.s3.gamerpc.dto.cpu.CPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.cpu.CPUResponse;
import nl.fontys.s3.gamerpc.dto.cpu.CPUUpdateRequest;
import nl.fontys.s3.gamerpc.persistence.entity.CPUEntity;

public class CPUConverter {
    private CPUConverter() { throw new IllegalStateException("Utility class");}
    public static CPU fromEntityToDomain(CPUEntity entity){
        return CPU.builder()
                .id(entity.getId())
                .model(entity.getModel())
                .manufacturer(entity.getManufacturer())
                .clockSpeed(entity.getClockSpeed())
                .turboSpeed(entity.getTurboSpeed())
                .cores(entity.getCores())
                .build();
    }

    public static CPUEntity fromDomainToEntity(CPU cpu){
        return CPUEntity.builder()
                .id((int) cpu.getId())
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .cores(cpu.getCores())
                .build();
    }

    public static CPU fromUpdateRequestToDomain(CPUUpdateRequest cpu){
        return CPU.builder()
                .id(cpu.getId())
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .cores(cpu.getCores())
                .build();
    }

    public static CPUEntity fromUpdateRequestToEntity(CPUUpdateRequest cpu){
        return CPUEntity.builder()
                .id((int) cpu.getId())
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .cores(cpu.getCores())
                .build();
    }

    public static CPUEntity fromSaveRequestToEntity (CPUSaveRequest cpu){
        return CPUEntity.builder()
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .cores(cpu.getCores())
                .build();
    }

    public static CPUResponse fromDomainToSaveResponse(CPU cpu){
        return CPUResponse.builder()
                .id(cpu.getId())
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .cores(cpu.getCores())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .build();
    }

    public static CPUResponse fromEntityToSaveResponse(CPUEntity cpu) {
        return CPUResponse.builder()
                .id(cpu.getId())
                .model(cpu.getModel())
                .manufacturer(cpu.getManufacturer())
                .cores(cpu.getCores())
                .clockSpeed(cpu.getClockSpeed())
                .turboSpeed(cpu.getTurboSpeed())
                .build();
    }
}
