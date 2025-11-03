package nl.fontys.s3.gamerpc.business.benchmark;

import nl.fontys.s3.gamerpc.dto.benchmark.BenchmarkSaveRequest;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkResultEntity;

import java.util.List;

public abstract class BenchmarkResult {
    public abstract List<BenchmarkResultEntity> process(BenchmarkSaveRequest request, BenchmarkEntity benchmark);
    public abstract String getType();
}
