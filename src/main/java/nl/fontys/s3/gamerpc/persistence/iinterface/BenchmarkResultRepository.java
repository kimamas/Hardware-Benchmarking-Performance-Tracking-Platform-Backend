package nl.fontys.s3.gamerpc.persistence.iinterface;

import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BenchmarkResultRepository extends JpaRepository<BenchmarkResultEntity, Long> {
    @Query("SELECT br.metricName, MIN(br.metricValue) " +
            "FROM BenchmarkResultEntity br " +
            "GROUP BY br.metricName")
    List<Object[]> findMinValueForEachTestType();
}
