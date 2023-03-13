package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Billing;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.UserInfo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
    Optional<ResidentHistory> findTopByUserInfo_IdOrderByIdDesc(long id);

    Optional<ResidentHistory> findByUserInfo_IdOrderByEndDate(long id);
    // all nhiều tháng
    @Query(value = "select reh.* from resident_history as reh inner join slot on reh.slot_id = slot.id " +
            "inner join room on slot.room_id = room.id inner join electric_water_usage as ewu on room.id = ewu.room_id " +
            "where  (ewu.created_date BETWEEN reh.start_date And reh.end_date) " +
            "And ewu.room_id=? and ewu.id=?",nativeQuery = true)
    List<ResidentHistory> findResidentsByRoomId(Long roomid, Long id);
    //all 1 tháng
    @Query(value = "select reh.* from resident_history as reh inner join slot on reh.slot_id = slot.id \n" +
            "            inner join room on slot.room_id = room.id \n" +
            "            where  room_id=?1 and (?2 BETWEEN reh.start_date and reh.end_date) and reh.checkin_date IS NOT NULL\n" +
            "            AND((?3 >= reh.checkin_date AND DATEADD(month, DATEDIFF(month, 0, ?4), 0) <= reh.end_date AND reh.checkout_date IS NULL)\n" +
            "            OR(?5 >= reh.checkin_date AND DATEADD(month, DATEDIFF(month, 0, ?6), 0)<= reh.checkout_date AND reh.checkout_date IS NOT NULl\n" +
            "            ))",nativeQuery = true)
    List<ResidentHistory> findResidentsByRoomIdInMonth(Long roomid , LocalDate month_pay1, LocalDate month_pay2,LocalDate month_pay3,LocalDate month_pay4,LocalDate month_pay5);


;
    @Query(value = "select * from resident_history where :date between start_date and end_date", nativeQuery = true)
    List<ResidentHistory> findByDateBetweenStartDateAndEndDate(@Param("date")Date date);

    @Query(value = "select * from resident_history where slot_id = :slot_id and (:date between start_date and end_date)", nativeQuery = true)
    ResidentHistory findBySlotIdWithDateBetweenStartDateAndEndDate(@Param("slot_id")long id,@Param("date") Date date);

    List<ResidentHistory> findBySlot_IdAndCheckinDateIsNull(Long slotId);

    List<ResidentHistory> findBySlot_IdAndCheckoutDateIsNull(Long slotId);
    @Query(value = "select top 1 * from resident_history where resident_id = :resident_id  and (GETDATE() between start_date and end_date)", nativeQuery = true)
    Optional<ResidentHistory> findCurrentSlotOfResident(@Param("resident_id") long residentId);

}
