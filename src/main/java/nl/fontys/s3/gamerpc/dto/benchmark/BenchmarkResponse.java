package nl.fontys.s3.gamerpc.dto.benchmark;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkResponse {
    private Long id;
    private Double overallScore;
    private String description;
    private Instant timestamp;
    private List<HardwareBenchmarkResponse> hardwareResults;
}
