package nl.fontys.s3.gamerpc.dto.ram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RAMResponse {
    private long id;
    private String model;
    private String manufacturer;
    private int ram;
    private int memorySpeed;
    private String type;
    private String timings;
    private int channels;
}
