package nl.fontys.s3.gamerpc.persistence.iinterface;

import nl.fontys.s3.gamerpc.persistence.entity.GPUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPURepository extends JpaRepository<GPUEntity, Long> {
    boolean existsByModel(String model);

}
