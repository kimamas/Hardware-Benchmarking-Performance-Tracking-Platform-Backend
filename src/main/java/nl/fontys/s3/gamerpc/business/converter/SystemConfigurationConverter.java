package nl.fontys.s3.gamerpc.business.converter;

import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationResponse;
import nl.fontys.s3.gamerpc.persistence.entity.SystemConfigurationEntity;

public class SystemConfigurationConverter {
    private SystemConfigurationConverter() {throw new IllegalStateException("Utility class");}
    public static SystemConfigurationResponse mapEntityToResponse(SystemConfigurationEntity entity) {
        SystemConfigurationResponse response = new SystemConfigurationResponse();
        response.setCpuId(entity.getCpu().getId());
        response.setGpuId(entity.getGpu().getId());
        response.setRamId(entity.getRam().getId());
        return response;
    }
}
