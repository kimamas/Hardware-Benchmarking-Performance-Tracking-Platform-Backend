package nl.fontys.s3.gamerpc.dto.benchmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RamBenchmarkResponse implements HardwareBenchmarkResponse {
    private Long id;
    private String ramModel;
    private Double memorySpeedTest;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getType() {
        return "RAM";
    }
}
