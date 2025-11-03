package nl.fontys.s3.gamerpc.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "ram")
public class RAMEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Size(max = 128)
    @NotNull
    @Column(name = "model", nullable = false, length = 128)
    private String model;

    @Size(max = 64)
    @Column(name = "manufacturer", length = 64)
    private String manufacturer;

    @NotNull
    @Column(name = "ram", nullable = false)
    private int ram;

    @NotNull
    @Column(name = "memory_speed", nullable = false)
    private int memorySpeed;

    @Size(max = 32)
    @NotNull
    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Size(max = 32)
    @Column(name = "timings", length = 32)
    private String timings;

    @Column(name = "channels")
    private int channels;

}