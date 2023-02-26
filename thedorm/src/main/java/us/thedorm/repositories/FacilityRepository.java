package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
