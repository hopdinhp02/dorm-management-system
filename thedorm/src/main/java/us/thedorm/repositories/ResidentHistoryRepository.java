package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Billing;
import us.thedorm.models.FacilityDetail;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.UserInfo;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
    Optional<ResidentHistory> findTopByUserInfo_IdOrderByEndDate(long id);

    Optional<ResidentHistory> findByUserInfo_IdOrderByEndDate(long id);

    Optional<ResidentHistory> findTopByUserInfo_IdOrderByIdDesc(long id);

    @Query(value = "select * from resident_history where :date between start_date and end_date", nativeQuery = true)
    List<ResidentHistory> findByDateBetweenStartDateAndEndDate(@Param("date") Date date);

    @Query(value = "select * from resident_history where slot_id = :slot_id and (:date between start_date and end_date)", nativeQuery = true)
    ResidentHistory findBySlotIdWithDateBetweenStartDateAndEndDate(@Param("slot_id") long id, @Param("date") Date date);

    List<ResidentHistory> findBySlot_IdAndCheckinDateIsNull(Long slotId);

    List<ResidentHistory> findBySlot_IdAndCheckoutDateIsNull(Long slotId);

    @Query(value = "select top 1 * from resident_history where resident_id = :resident_id  and (GETDATE() between start_date and end_date)", nativeQuery = true)
    Optional<ResidentHistory> findCurrentSlotOfResident(@Param("resident_id") long residentId);




    @Query(value = "select  rh.*from resident_history as rh inner join user_info as uf on rh.resident_id = uf.id\n" +
            "   where uf.name like '%'+:name +'%' and GETDATE() between rh.start_date and rh.end_date", nativeQuery = true)
    List<ResidentHistory> findResidentHistoriesByName(@Param("name") String name);


    @Query(value = "    select rh.*from resident_history as rh \n" +
            "    inner join user_info as uf on rh.resident_id = uf.id\n" +
            "\tinner join slot sl on rh.slot_id = sl.id            \n" +
            "   where  uf.name like '%'+:name +'%' and GETDATE() between rh.start_date and rh.end_date and  sl.room_id =:room_id", nativeQuery = true)
    List<ResidentHistory> findResidentHistoriesByNameAndRoomId(@Param("name") String name, @Param("room_id") long id);

    @Query(value = "select  rh.*from resident_history as rh inner join user_info as uf on rh.resident_id = uf.id\n" +
            "   where uf.name like '%'+:name +'%' and GETDATE() between rh.start_date and rh.end_date and rh.slot_id =:slot_id", nativeQuery = true)
    List<ResidentHistory> findResidentHistoriesByNameAndSlotId(@Param("name") String name, @Param("slot_id") long id);



    @Query(value = " select rh.*from resident_history as rh \n" +
            "    inner join user_info as uf on rh.resident_id = uf.id\n" +
            "\tinner join slot sl on rh.slot_id = sl.id\n" +
            "                 inner join room room on sl.room_id = room.id\n" +
            "   where  uf.name like '%'+:name +'%'and GETDATE() between rh.start_date and rh.end_date and room.dorm_id =:dorm_id", nativeQuery = true)
    List<ResidentHistory> findResidentHistoriesByNameAndDormId(@Param("name") String name, @Param("dorm_id") long id);


    @Query(value = "   select rh.*from resident_history as rh \n" +
            "    inner join user_info as uf on rh.resident_id = uf.id\n" +
            "\tinner join slot sl on rh.slot_id = sl.id\n" +
            "                 inner join room room on sl.room_id = room.id\n" +
            "\t\t\t\t inner join dorm dorm on room.dorm_id=dorm.id\n" +
            "   where  uf.name like '%'+:name +'%' and GETDATE() between rh.start_date and rh.end_date and dorm.branch_id = :branch_id", nativeQuery = true)
    List<ResidentHistory> findResidentHistoriesByNameAndBranchId(@Param("name") String name, @Param("branch_id") long id);
}