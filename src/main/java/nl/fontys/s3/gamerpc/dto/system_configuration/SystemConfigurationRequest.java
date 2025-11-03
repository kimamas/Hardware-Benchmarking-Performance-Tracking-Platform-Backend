package nl.fontys.s3.gamerpc.dto.system_configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemConfigurationRequest {
    private int ramId;
    private int cpuId;
    private int gpuId;
}
