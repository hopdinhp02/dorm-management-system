package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Facility;
import us.thedorm.models.FacilityDetail;

public interface FacilityDetailRepository extends JpaRepository<FacilityDetail, Long> {
}
