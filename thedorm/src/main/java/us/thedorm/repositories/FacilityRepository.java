package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Facility;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findBySlot_Id(Long slotId);
    List<Facility> findByRoom_Id(Long roomId);
}
