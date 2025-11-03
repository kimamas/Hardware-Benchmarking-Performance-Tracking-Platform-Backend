package nl.fontys.s3.gamerpc.business;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.benchmark.BenchmarkResultFactory;
import nl.fontys.s3.gamerpc.business.converter.BenchmarkConverter;
import nl.fontys.s3.gamerpc.dto.benchmark.*;
import nl.fontys.s3.gamerpc.persistence.iinterface.*;
import nl.fontys.s3.gamerpc.persistence.entity.*;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class BenchmarkService {
private final BenchmarkRepository benchmarkRepository;
private final UserRepository userRepository;
private final BenchmarkResultFactory processorFactory;
private final BenchmarkConverter benchmarkConverter;
private final BenchmarkResultRepository benchmarkResultRepository;

    public BenchmarkResponse saveBenchmark(BenchmarkSaveRequest request) {
        BenchmarkEntity benchmark = new BenchmarkEntity();

        if(request.getUserId() != -1) {
            benchmark.setUser(userRepository.findById((long) request.getUserId()).orElse(null));
        }

        benchmark.setCreatedAt(request.getCreationDate());

        List<String> hardwareTypes = request.getHardwareBenchmarks().stream()
                .map(HardwareBenchmarkResultRequest::getType)
                .distinct()
                .toList();

        List<BenchmarkResultEntity> results = hardwareTypes.stream()
                .flatMap(type -> processorFactory.getProcessor(type).process(request, benchmark).stream())
                .toList();

        Double overallScore = calculateScore(results);
        benchmark.setOverallScore(overallScore);
        benchmark.setResults(results);
        benchmark.setDescription(makeDescription(overallScore));
        BenchmarkEntity savedBenchmark = benchmarkRepository.save(benchmark);

        return benchmarkConverter.mapToBenchmarkResponse(savedBenchmark);
    }

    private @Size(max = 255) String makeDescription(Double overallScore) {
        StringBuilder description = new StringBuilder();
        description.append("Based on your system's overall performance score of ").append(String.format("%.2f", overallScore)).append("%, ");

        if (overallScore >= 90) {
            description.append("your PC is high-end and suitable for running the latest AAA games at ultra settings, as well as handling resource-heavy applications like video editing, 3D rendering, and software development. You can comfortably play games like:\n")
                    .append("- Cyberpunk 2077 (Ultra settings)\n")
                    .append("- Red Dead Redemption 2 (Ultra settings)\n")
                    .append("- Call of Duty: Warzone (Ultra settings)\n")
                    .append("For productivity, you can multitask efficiently with heavy applications running simultaneously, making it ideal for creative professionals, developers, and gamers.");
        } else if (overallScore >= 75) {
            description.append("your PC is a solid mid-range machine that can handle AAA games at high settings with a smooth experience. Games like:\n")
                    .append("- The Witcher 3 (High settings)\n")
                    .append("- Fortnite (High settings)\n")
                    .append("- Minecraft (High settings)\n")
                    .append("can be played without issues. It's also great for productivity tasks like coding, design, and video editing.");
        } else if (overallScore >= 50) {
            description.append("your system is capable of handling older games and light multitasking. Games like:\n")
                    .append("- Team Fortress 2 (Medium settings)\n")
                    .append("- Counter-Strike: Global Offensive (Medium settings)\n")
                    .append("- Rocket League (Medium settings)\n")
                    .append("should perform adequately. For work, it can handle office productivity apps, basic coding, and light video editing.");
        } else {
            description.append("your PC is best suited for basic tasks such as browsing, word processing, and light multimedia consumption. It may run very light or older games, but anything more demanding may cause lag.");
        }

        return description.toString();
    }

    private double getMinMetricValue(List<Object[]> minValues, String metricName, double defaultValue) {
        for (Object[] result : minValues) {
            String retrievedMetricName = (String) result[0];
            Double retrievedMinValue = (Double) result[1];

            if (metricName.equals(retrievedMetricName)) {
                return retrievedMinValue;
            }
        }
        return defaultValue;
    }

    private Double calculateScore(List<BenchmarkResultEntity> results) {
        List<Object[]> minValues = benchmarkResultRepository.findMinValueForEachTestType();

        double gpuGamingScore = 0;
        double gpuShaderScore = 0;
        double gpuMemoryScore = 0;
        double cpuSingleCoreScore = 0;
        double cpuMultiCoreScore = 0;
        double ramScore = 0;

        for (BenchmarkResultEntity benchmarkResult : results) {
            switch (benchmarkResult.getMetricName()) {
                case "Gaming Test":
                    gpuGamingScore = getMinMetricValue(minValues, "Gaming Test", 40) / benchmarkResult.getMetricValue()  * 100;
                    break;
                case "Shader Test":
                    gpuShaderScore =  getMinMetricValue(minValues, "Shader Test", 10) / benchmarkResult.getMetricValue() * 100;
                    break;
                case "Memory Test":
                    gpuMemoryScore =  getMinMetricValue(minValues, "Memory Test", 600) / benchmarkResult.getMetricValue() * 100;
                    break;
                case "Single-Core Test":
                    cpuSingleCoreScore = getMinMetricValue(minValues, "Single-Core Test", 700) / benchmarkResult.getMetricValue() * 100;
                    break;
                case "Multi-Core Test":
                    cpuMultiCoreScore =  getMinMetricValue(minValues, "Multi-Core Test", 60) / benchmarkResult.getMetricValue() * 100;
                    break;
                case "Memory-Speed Test":
                    ramScore =  getMinMetricValue(minValues, "Memory-Speed Test", 150) / benchmarkResult.getMetricValue() * 100;
                    break;
                    default: break;
            }
        }

        double gpuScorePercentage = (gpuGamingScore + gpuShaderScore + gpuMemoryScore) / 3;
        double cpuScorePercentage = (cpuSingleCoreScore + cpuMultiCoreScore) / 2;

        return (gpuScorePercentage + cpuScorePercentage + ramScore) / 3;

    }

    @Transactional
    public List<BenchmarkResponse> getBenchmarksByUserId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID: " + id);
        }
            List<BenchmarkEntity> benchmarkEntities = benchmarkRepository.findByUserId(Math.toIntExact(id));
            if (benchmarkEntities.isEmpty()) {
                throw new IllegalArgumentException("No benchmarks found for user ID: " + id);
            }
            return benchmarkEntities.stream()
                    .map(benchmarkConverter::mapToBenchmarkResponse).toList();
    }

    public List<BenchmarkResponse> getBenchmarks() {
        List<BenchmarkEntity> benchmarkEntities = benchmarkRepository.findAllBenchmarks();
        return benchmarkEntities.stream()
                .map(benchmarkConverter::mapToBenchmarkResponse).toList();
    }
}
