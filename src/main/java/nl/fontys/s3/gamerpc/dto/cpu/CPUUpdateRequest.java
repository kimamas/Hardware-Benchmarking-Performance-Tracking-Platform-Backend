package nl.fontys.s3.gamerpc.dto.cpu;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CPUUpdateRequest {
    @NotNull
    private long id;

    @NotNull(message = "Model cannot be blank")
    private String model;

    @Size(max = 64, message = "Manufacturer cannot exceed 64 characters")
    private String manufacturer;
    @NotNull(message = "Cores cannot be empty")
    @Min(value = 1, message = "Minimum number of cores is 1")
    @Max(value = 128, message = "Maximum number of cores is 128")
    private int cores;

    @NotNull(message = "Clock Speed cannot be empty")
    @Positive(message = "Minimum Clock Speed is 1 GHz")
    @Max(value = 10, message = "Maximum Clock Speed is 10 GHz")
    private double clockSpeed;
    @NotNull(message = "Turbo Speed cannot be empty")
    @Positive(message = "Minimum Turbo Speed is 1 GHz")
    @Max(value = 10, message = "Maximum Turbo Speed is 10 GHz")
    private double turboSpeed;
}