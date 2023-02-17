package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Billing;

public interface BillingRepository extends JpaRepository<Billing,Long> {
}
