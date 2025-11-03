package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GpuBenchmark {
    private Integer id;

    private GPUEntity gpu;

    private BenchmarkEntity benchmarkEntity;

    private Double gamingTest;

    private Double shaderTest;

    private Double memoryTest;

    private Instant createdAt;
}
