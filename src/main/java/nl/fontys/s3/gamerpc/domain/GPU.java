package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GPU {
    private long id;
    private String model;
    private String manufacturer;
    private int ram;
    private int clockSpeed;
    private int turboSpeed;
}