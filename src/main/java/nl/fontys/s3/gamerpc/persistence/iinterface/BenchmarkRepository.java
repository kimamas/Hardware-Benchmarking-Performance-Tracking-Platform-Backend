package nl.fontys.s3.gamerpc.persistence.iinterface;

import nl.fontys.s3.gamerpc.persistence.entity.BenchmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkRepository extends JpaRepository<BenchmarkEntity, Long> {
    @Query("SELECT b FROM BenchmarkEntity b LEFT JOIN FETCH b.results WHERE b.user.id = :userId")
    List<BenchmarkEntity> findByUserId(@Param("userId") int userId);

    @Query("SELECT b FROM BenchmarkEntity b LEFT JOIN FETCH b.results ORDER BY b.id DESC")
    List<BenchmarkEntity> findAllBenchmarks();
}
