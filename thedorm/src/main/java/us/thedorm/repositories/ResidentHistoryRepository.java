package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    @Query(value = "select reh.* from resident_history as reh inner join slot on reh.slot_id = slot.id " +
            "inner join room on slot.room_id = room.id " +
            "where  room_id=?  and (? BETWEEN reh.start_date And reh.end_date)",nativeQuery = true)
    List<ResidentHistory> findResidentsByRoomIdInMonth(Long roomid , LocalDate month_pay );


}
