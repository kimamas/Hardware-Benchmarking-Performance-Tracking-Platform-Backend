package nl.fontys.s3.gamerpc.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cpu")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CPUEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String model;

    @Column
    private String manufacturer;

    @Column(nullable = false)
    private int cores;

    @Column(nullable = false, name = "clock_speed")
    private double clockSpeed;

    @Column(nullable = false, name = "turbo_speed")
    private double turboSpeed;
}
