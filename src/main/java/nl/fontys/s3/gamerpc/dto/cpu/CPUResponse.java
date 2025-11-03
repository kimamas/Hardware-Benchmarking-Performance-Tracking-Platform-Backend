package nl.fontys.s3.gamerpc.dto.cpu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CPUResponse {
    private long id;
    private String model;
    private String manufacturer;
    private int cores;
    private double clockSpeed;
    private double turboSpeed;
}
