package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.ElectricWaterUsage;

public interface ElectricWaterUsageRepo extends JpaRepository<ElectricWaterUsage,Long> {
}
