package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.Billing;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;

public interface BillingRepository extends JpaRepository<Billing,Long> {

}
