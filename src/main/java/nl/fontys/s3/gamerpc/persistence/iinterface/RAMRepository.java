package nl.fontys.s3.gamerpc.persistence.iinterface;

import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RAMRepository extends JpaRepository<RAMEntity, Long> {
    boolean existsByModel(String model);

}
