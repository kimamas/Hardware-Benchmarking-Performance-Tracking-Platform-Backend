package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RAM {
    private long id;
    private String model;
    private String manufacturer;
    private int numberOfRam;
    private int memorySpeed;
    private String type;
    private String timings;
    private int channels;
}
