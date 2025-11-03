package nl.fontys.s3.gamerpc.dto.ram;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RAMSaveRequest {
    @NotBlank(message = "Model cannot be blank")
    @Size(max = 128, message = "Model cannot exceed 128 characters")
    private String model;

    @Size(max = 64, message = "Manufacturer cannot exceed 64 characters")
    private String manufacturer;

    @NotNull(message = "RAM capacity cannot be null")
    @Min(value = 1, message = "RAM capacity must be at least 1 GB")
    @Max(value = 2048, message = "RAM capacity cannot exceed 2048 GB")
    private Integer ram;

    @NotNull(message = "Memory speed cannot be null")
    @Min(value = 100, message = "Memory speed must be at least 100 MHz")
    @Max(value = 10000, message = "Memory speed cannot exceed 10,000 MHz")
    private Integer memorySpeed;

    @NotBlank(message = "Type cannot be blank")
    @Size(max = 32, message = "Type cannot exceed 32 characters")
    private String type;

    @Size(max = 32, message = "Timings cannot exceed 32 characters")
    private String timings;

    @Min(value = 1, message = "Channels must be at least 1")
    @Max(value = 16, message = "Channels cannot exceed 16")
    private Integer channels;
}
