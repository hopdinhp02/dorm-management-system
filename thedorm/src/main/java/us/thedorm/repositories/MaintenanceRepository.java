package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.FacilityDetail;
import us.thedorm.models.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
