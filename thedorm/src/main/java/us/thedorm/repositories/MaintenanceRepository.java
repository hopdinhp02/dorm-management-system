package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.FacilityDetail;
import us.thedorm.models.Maintenance;

import java.util.Date;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query(value = "select * from maintenance where facility_detail_id = :facility_detail_id and CAST(fix_date as date) = :date", nativeQuery = true)
    List<Maintenance> findByFacilityDetailAndFixDate(@Param("facility_detail_id")Long facilityDetailId, @Param("date") Date date);

    @Query(value = "select * from maintenance where CAST(fix_date as date) = :date", nativeQuery = true)
    List<Maintenance> findByFixDate( @Param("date") Date date);
}
