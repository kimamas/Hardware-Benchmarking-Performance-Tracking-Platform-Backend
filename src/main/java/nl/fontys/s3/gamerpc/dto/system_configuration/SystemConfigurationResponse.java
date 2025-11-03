package nl.fontys.s3.gamerpc.dto.system_configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfigurationResponse {
    private int ramId;
    private int cpuId;
    private int gpuId;
}
