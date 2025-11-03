package nl.fontys.s3.gamerpc.business.benchmark;

import nl.fontys.s3.gamerpc.dto.benchmark.BenchmarkSaveRequest;
import nl.fontys.s3.gamerpc.dto.benchmark.HardwareBenchmarkResultRequest;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkResultEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class CpuBenchmarkResult extends BenchmarkResult {
    @Override
    public String getType() {
        return "CPU";
    }
    private String convertMetricName(String metricKey) {
        return switch (metricKey) {
            case "singleCoreTest" -> "Single-Core Test";
            case "multiCoreTest" -> "Multi-Core Test";
            default -> "Unknown Metric";
        };
    }
    @Override
    public List<BenchmarkResultEntity> process(BenchmarkSaveRequest request, BenchmarkEntity benchmark) {
        List<BenchmarkResultEntity> results = new ArrayList<>();

        for (HardwareBenchmarkResultRequest hardwareBenchmark : request.getHardwareBenchmarks()) {
            if(Objects.equals(hardwareBenchmark.getType(), "CPU"))
            {
                for (Map.Entry<String, Double> metric : hardwareBenchmark.getMetrics().entrySet()) {
                    BenchmarkResultEntity result = new BenchmarkResultEntity();
                    result.setBenchmark(benchmark);
                    result.setHardwareType(hardwareBenchmark.getType());

                    if(hardwareBenchmark.getHardwareId() != null) result.setHardwareId(Math.toIntExact(hardwareBenchmark.getHardwareId()));
                    result.setMetricName(convertMetricName(metric.getKey()));
                    result.setMetricValue(metric.getValue());

                    result.setCreatedAt(request.getCreationDate());

                    results.add(result);
                }
            }
        }
        return results;
    }
}
