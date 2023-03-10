package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Facility;
import us.thedorm.models.FacilityHistory;

import java.util.Optional;

public interface FacilityHistoryRepository extends JpaRepository<FacilityHistory, Long> {

    Optional<FacilityHistory> findTopByFacility_IdOrderByIdDesc(Long facilityId);
}
