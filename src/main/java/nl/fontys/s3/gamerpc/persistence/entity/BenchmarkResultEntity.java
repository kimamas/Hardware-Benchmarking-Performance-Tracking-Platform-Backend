package nl.fontys.s3.gamerpc.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "benchmark_result")
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "benchmark_id", nullable = false)
    private BenchmarkEntity benchmark;

    @Column(name = "hardware_id")
    private Integer hardwareId;

    @NotNull
    @Lob
    @Column(name = "hardware_type", nullable = false)
    private String hardwareType;

    @Size(max = 50)
    @NotNull
    @Column(name = "metric_name", nullable = false, length = 50)
    private String metricName;

    @NotNull
    @Column(name = "metric_value", nullable = false)
    private Double metricValue;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}