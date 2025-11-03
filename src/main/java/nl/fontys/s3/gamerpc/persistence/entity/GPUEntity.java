package nl.fontys.s3.gamerpc.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gpu")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GPUEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String model;

    @Column
    private String manufacturer;

    @Column(nullable = false, name = "ram")
    private int ram;

    @Column(nullable = false, name = "clock_speed")
    private int clockSpeed;

    @Column(nullable = false, name = "turbo_speed")
    private int turboSpeed;
}
