package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import us.thedorm.models.Facility;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findBySlot_Id(Long slotId);
    List<Facility> findByRoom_Id(Long roomId);

    List<Facility> findByDorm_Id(Long dormId);

    List<Facility> findByBranch_Id(Long branchId);

    @Query(value = "select * from facility where branch_id is null and dorm_id is null and room_id is null and slot_id is null", nativeQuery = true)
    List<Facility> findNotAssignFacility();
}
