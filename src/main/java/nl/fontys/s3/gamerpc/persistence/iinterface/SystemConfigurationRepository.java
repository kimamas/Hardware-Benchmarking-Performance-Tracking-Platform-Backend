package nl.fontys.s3.gamerpc.persistence.iinterface;

import nl.fontys.s3.gamerpc.persistence.entity.SystemConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfigurationEntity, Long> {
    Optional<SystemConfigurationEntity> findByUserId(int userId);
}
