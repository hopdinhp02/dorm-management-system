package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Bed;
import us.thedorm.models.Billing;
import us.thedorm.models.StatusBed;
import us.thedorm.models.TypeBilling;

import java.util.List;
import java.util.Optional;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> getBedsByRoom_Id(Long roomId);

    List<Bed> getBedsByRoom_IdAndStatus(long id, StatusBed status);
}
