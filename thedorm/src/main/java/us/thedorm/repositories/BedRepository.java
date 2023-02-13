package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.bed;
import us.thedorm.models.branch;

public interface BedRepository extends JpaRepository<bed, Long> {
}
