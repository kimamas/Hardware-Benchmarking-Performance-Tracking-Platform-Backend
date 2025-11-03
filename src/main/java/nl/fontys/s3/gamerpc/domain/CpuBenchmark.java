package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import nl.fontys.s3.gamerpc.persistence.entity.CPUEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpuBenchmark {
    private Integer id;

    private CPUEntity cpu;

    private BenchmarkEntity benchmarkEntity;

    private Double singleCoreTest;

    private Double multiCoreTest;

    private Instant createdAt;
}
