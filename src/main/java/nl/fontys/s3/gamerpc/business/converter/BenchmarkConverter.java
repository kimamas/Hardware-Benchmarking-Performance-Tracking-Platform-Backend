package nl.fontys.s3.gamerpc.business.converter;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.dto.benchmark.*;
import nl.fontys.s3.gamerpc.persistence.iinterface.CPURepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.GPURepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.RAMRepository;
import nl.fontys.s3.gamerpc.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BenchmarkConverter {
    private final CPURepository cpuRepository;
    private final GPURepository gpuRepository;
    private final RAMRepository ramRepository;

    private List<HardwareBenchmarkResponse> mapToSpecificHardwareBenchmarkResponse(List<BenchmarkResultEntity> results) {
        if (results == null || results.isEmpty()) {
            throw new IllegalArgumentException("Results list cannot be null or empty.");
        }
        Map<String, List<BenchmarkResultEntity>> groupedResults = results.stream()
                .collect(Collectors.groupingBy(BenchmarkResultEntity::getHardwareType));

        return groupedResults.values().stream()
                .map(this::mapGroupedResultsToResponse)
                .toList();
    }

    private HardwareBenchmarkResponse mapGroupedResultsToResponse(List<BenchmarkResultEntity> groupedResults) {
        if (groupedResults.isEmpty()) {
            throw new IllegalArgumentException("Grouped results cannot be empty.");
        }

        BenchmarkResultEntity firstResult = groupedResults.get(0);
        String hardwareType = firstResult.getHardwareType();

        return switch (hardwareType) {
            case "CPU" -> mapCpuGroupedResultsToResponse(groupedResults);
            case "GPU" -> mapGpuGroupedResultsToResponse(groupedResults);
            case "RAM" -> mapRamGroupedResultsToResponse(groupedResults);
            default -> throw new IllegalArgumentException("Unknown hardware type: " + hardwareType);
        };
    }

    private CpuBenchmarkResponse mapCpuGroupedResultsToResponse(List<BenchmarkResultEntity> groupedResults) {
        CpuBenchmarkResponse response = new CpuBenchmarkResponse();
        response.setId(Long.valueOf(groupedResults.get(0).getId()));

        Integer hardwareId = groupedResults.get(0).getHardwareId();
        if (hardwareId == null) {
            response.setCpuModel("Unknown CPU");
        }else {
            CPUEntity cpu = cpuRepository.findById(Long.valueOf(hardwareId)).orElse(null);
            if (cpu != null) {
                response.setCpuModel(cpu.getModel());
            } else {
                response.setCpuModel("Unknown CPU");
            }
        }

        groupedResults.forEach(result -> {
            switch (result.getMetricName()) {
                case "Single-Core Test" -> response.setSingleCoreTest(result.getMetricValue());
                case "Multi-Core Test" -> response.setMultiCoreTest(result.getMetricValue());
                default -> throw new IllegalArgumentException("Unknown CPU metric: " + result.getMetricName());
            }
        });
        return response;
    }

    private GpuBenchmarkResponse mapGpuGroupedResultsToResponse(List<BenchmarkResultEntity> groupedResults) {
        GpuBenchmarkResponse response = new GpuBenchmarkResponse();
        response.setId(Long.valueOf(groupedResults.get(0).getId()));

        Integer hardwareId = groupedResults.get(0).getHardwareId();
        if (hardwareId == null) {
            response.setGpuModel("Unknown GPU");
        }else {
            GPUEntity gpu = gpuRepository.findById(Long.valueOf(hardwareId)).orElse(null);
            if (gpu != null) {
                response.setGpuModel(gpu.getModel());
            } else {
                response.setGpuModel("Unknown GPU");
            }
        }

        groupedResults.forEach(result -> {
            switch (result.getMetricName()) {
                case "Gaming Test" -> response.setGamingTest(result.getMetricValue());
                case "Shader Test" -> response.setShaderTest(result.getMetricValue());
                case "Memory Test" -> response.setMemoryTest(result.getMetricValue());
                default -> throw new IllegalArgumentException("Unknown GPU metric: " + result.getMetricName());
            }
        });
        return response;
    }

    private RamBenchmarkResponse mapRamGroupedResultsToResponse(List<BenchmarkResultEntity> groupedResults) {
        RamBenchmarkResponse response = new RamBenchmarkResponse();
        response.setId(Long.valueOf(groupedResults.get(0).getId()));

        Integer hardwareId = groupedResults.get(0).getHardwareId();
        if (hardwareId == null) {
            response.setRamModel("Unknown RAM");
        }else {
            RAMEntity ram = ramRepository.findById(Long.valueOf(hardwareId)).orElse(null);
            if (ram != null) {
                response.setRamModel(ram.getModel());
            } else {
                response.setRamModel("Unknown RAM");
            }
        }

        groupedResults.forEach(result -> {
            if ("Memory-Speed Test".equals(result.getMetricName())) {
                response.setMemorySpeedTest(result.getMetricValue());
            }
            else {
                throw new IllegalArgumentException("Unknown RAM metric: " + result.getMetricName());
            }
        });
        return response;
    }
    public BenchmarkResponse mapToBenchmarkResponse(BenchmarkEntity benchmark) {
        BenchmarkResponse response = new BenchmarkResponse();
        response.setId(Long.valueOf(benchmark.getId()));
        response.setOverallScore(benchmark.getOverallScore());
        response.setTimestamp(benchmark.getCreatedAt());
        response.setDescription(benchmark.getDescription());
        response.setHardwareResults(mapToSpecificHardwareBenchmarkResponse(benchmark.getResults()));
        return response;
    }
}
