package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CPU {
    private long id;
    private String model;
    private String manufacturer;
    private int cores;
    private double clockSpeed;
    private double turboSpeed;

}