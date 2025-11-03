package nl.fontys.s3.gamerpc.dto.benchmark;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardwareBenchmarkResultRequest {
    @NotNull(message = "Hardware type is required")
    private String type;

    private Long hardwareId;

    @NotNull(message = "Metrics are required")
    private Map<String, Double> metrics;
}
