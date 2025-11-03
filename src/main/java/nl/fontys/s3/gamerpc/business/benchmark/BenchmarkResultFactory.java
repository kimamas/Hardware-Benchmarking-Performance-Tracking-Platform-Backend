package nl.fontys.s3.gamerpc.business.benchmark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Component
public class BenchmarkResultFactory {
    private final Map<String, BenchmarkResult> processors;

    @Autowired
    public BenchmarkResultFactory(List<BenchmarkResult> processorList) {
        this.processors = processorList.stream()
                .collect(Collectors.toMap(BenchmarkResult::getType, Function.identity()));

    }

    public BenchmarkResult getProcessor(String type) {
        BenchmarkResult processor = processors.get(type);
        if (processor == null) {
            throw new IllegalArgumentException("No processor registered for hardware type: " + type);
        }
        return processor;
    }
}

