package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.billing;

public interface BillingRepository extends JpaRepository<billing,Long> {
}
