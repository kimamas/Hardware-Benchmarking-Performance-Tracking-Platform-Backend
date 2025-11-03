package nl.fontys.s3.gamerpc.dto.benchmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GpuBenchmarkResponse implements HardwareBenchmarkResponse {
    private Long id;
    private String gpuModel;
    private Double gamingTest;
    private Double shaderTest;
    private Double memoryTest;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getType() {
        return "GPU";
    }
}
