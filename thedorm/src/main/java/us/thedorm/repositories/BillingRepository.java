package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Billing;

import java.util.List;
import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing,Long> {
    Optional<Billing> findTopByUserInfo_IdAndTypeOrderByIdDesc(long id, Billing.Type type);

    List<Billing> findAllByUserInfo_Id(long id);
}
