package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RamBenchmark {
    private Integer id;

    private RAMEntity ram;

    private BenchmarkEntity benchmarkEntity;

    private Double memorySpeedTest;

    private Instant createdAt;
}
