package nl.fontys.s3.gamerpc.dto.gpu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GPUResponse {
    private long id;
    private String model;
    private String manufacturer;
    private int ram;
    private int clockSpeed;
    private int turboSpeed;
}
