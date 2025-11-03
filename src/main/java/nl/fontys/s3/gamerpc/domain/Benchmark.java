package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.s3.gamerpc.persistence.entity.UserEntity;

import java.time.Instant;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Benchmark {
    private Integer id;

    private UserEntity user;

    private Double overallScore;

    private Instant createdAt;

    private String description;

}
