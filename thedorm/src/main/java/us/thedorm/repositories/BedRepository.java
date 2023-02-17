package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Bed;

public interface BedRepository extends JpaRepository<Bed, Long> {
}
