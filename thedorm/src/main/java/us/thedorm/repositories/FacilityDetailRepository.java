package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Facility;
import us.thedorm.models.FacilityDetail;
import us.thedorm.models.ResidentHistory;

import java.util.Date;
import java.util.List;

public interface FacilityDetailRepository extends JpaRepository<FacilityDetail, Long> {

    @Query(value = "select fd.* from facility f\n" +
            "inner join facility_detail fd on f.facility_detail_id = fd.id\n" +
            "inner join facility_history fh on fh.facility_id =f.id\n" +
            "where fh.slot_id = :slot_id and ((end_date is NULL and :date > start_date) or (:date between start_date and end_date))", nativeQuery = true)
    List<FacilityDetail> findFacilityBySlotIdWithDateBetweenStartDateAndEndDate(@Param("slot_id") Long slotId, @Param("date") Date date);

    @Query(value = "select fd.* from facility f\n" +
            "inner join facility_detail fd on f.facility_detail_id = fd.id\n" +
            "inner join facility_history fh on fh.facility_id =f.id\n" +
            "where fh.room_id = :room_id and ((end_date is NULL and :date > start_date) or (:date between start_date and end_date))", nativeQuery = true)
    List<FacilityDetail> findFacilityByRoomIdWithDateBetweenStartDateAndEndDate(@Param("room_id") Long roomId, @Param("date") Date date);

    @Query(value = "select fd.* from facility f\n" +
            "inner join facility_detail fd on f.facility_detail_id = fd.id\n" +
            "inner join facility_history fh on fh.facility_id =f.id\n" +
            "where fh.dorm_id = :dorm_id and ((end_date is NULL and :date > start_date) or (:date between start_date and end_date))", nativeQuery = true)
    List<FacilityDetail> findFacilityByDormIdWithDateBetweenStartDateAndEndDate(@Param("dorm_id") Long dormId, @Param("date") Date date);

    @Query(value = "select fd.* from facility f\n" +
            "inner join facility_detail fd on f.facility_detail_id = fd.id\n" +
            "inner join facility_history fh on fh.facility_id =f.id\n" +
            "where fh.branch_id = :branch_id and ((end_date is NULL and :date > start_date) or (:date between start_date and end_date))", nativeQuery = true)
    List<FacilityDetail> findFacilityByBranchIdWithDateBetweenStartDateAndEndDate(@Param("branch_id") Long branchId, @Param("date") Date date);

}
