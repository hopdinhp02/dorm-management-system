package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.Billing;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;
@Repository
@Transactional
public interface BillingRepository extends JpaRepository<Billing,Long> {
    @Modifying
    @Query(value = "insert into billing(type,cost,status,resident_id) values(?1,?2,?3,?4);", nativeQuery = true)
    void insertBilling(TypeBilling typeBilling, int cost, StatusBilling statusBilling,long resident_id);
}
