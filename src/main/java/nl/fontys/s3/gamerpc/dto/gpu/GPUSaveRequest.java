package nl.fontys.s3.gamerpc.dto.gpu;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GPUSaveRequest {
    @NotNull(message = "Model cannot be blank")
    private String model;

    @Size(max = 64, message = "Manufacturer cannot exceed 64 characters")
    private String manufacturer;
    @NotNull(message = "Ram cannot be empty")
    @Min(value = 1, message = "Minimum Ram is 1 GB")
    @Max(value = 200, message = "Maximum Ram is 200 GB")
    private int ram;

    @NotNull(message = "Frequency cannot be empty")
    @Min(value = 200, message = "Minimum frequency is 200 MHz")
    @Max(value = 25000, message = "Maximum frequency is 25 GHz")
    private int clockSpeed;

    @NotNull(message = "Frequency cannot be empty")
    @Min(value = 200, message = "Minimum frequency is 200 MHz")
    @Max(value = 25000, message = "Maximum frequency is 25 GHz")
    private int turboSpeed;
}
