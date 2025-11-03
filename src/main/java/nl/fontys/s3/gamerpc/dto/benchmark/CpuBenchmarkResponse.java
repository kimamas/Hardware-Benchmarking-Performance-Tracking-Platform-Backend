package nl.fontys.s3.gamerpc.dto.benchmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpuBenchmarkResponse implements HardwareBenchmarkResponse {
    private Long id;
    private String cpuModel;
    private double singleCoreTest;
    private double multiCoreTest;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getType() {
        return "CPU";
    }
}
