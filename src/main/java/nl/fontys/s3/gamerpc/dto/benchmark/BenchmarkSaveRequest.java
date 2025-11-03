package nl.fontys.s3.gamerpc.dto.benchmark;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkSaveRequest {
    private int userId =-1;

    @NotNull(message = "Creation date is required")
    private Instant creationDate;

    @NotNull(message = "Benchmark results are required")
    @Size(min = 1, message = "At least one benchmark result is required")
    private List<HardwareBenchmarkResultRequest> hardwareBenchmarks;
}
