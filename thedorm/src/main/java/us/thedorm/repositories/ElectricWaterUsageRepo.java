package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.ElectricWaterUsage;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.Room;
import us.thedorm.models.UserInfo;
//import us.thedorm.models.Slot;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ElectricWaterUsageRepo extends JpaRepository<ElectricWaterUsage,Long> {
//    List<ElectricWaterUsage> findByRoom_Id(Long room_id);
    @Query(value = "Select * from electric_water_usage" +
            "            where room_id =?",nativeQuery = true)
    List<ElectricWaterUsage> findAllByRoomId(Long room_id);

    // điện nước của 1 phòng trong 1 kì

    @Query(value = "select ewu.* from resident_history as reh inner join slot on reh.slot_id = slot.id " +
            "inner join room on slot.room_id = room.id inner join electric_water_usage as ewu on room.id = ewu.room_id" +
            " where  (ewu.created_date BETWEEN reh.start_date And reh.end_date) And room.id=?",nativeQuery = true)
    List<ElectricWaterUsage> findElectricWaterUsagesByRoomId(Long room_id);

    @Query(value = "select ewu.*,reh.resident_id from resident_history as reh inner join slot on reh.slot_id = slot.id \n" +
            "inner join room on slot.room_id = room.id \n" +
            "inner join electric_water_usage as ewu on room.id = ewu.room_id \n" +
            "where  Month(ewu.month_pay) =? and YEAR(ewu.month_pay) =? and ewu.room_id =? and slot.[status]='NotAvailable' and ewu.id=? and resident_id=? ",nativeQuery = true)
    ResidentHistory ElecWaterOfRoomIdAndResidentInMonth(int month,int year,Long roomId,Long ewuId,Long residentId);

    @Query(value = "select ewu.* from  room "+
            "inner join electric_water_usage as ewu on room.id = ewu.room_id \n" +
            "where  Month(ewu.month_pay) =? and YEAR(ewu.month_pay) =? and ewu.room_id =?  ",nativeQuery = true)
    List<ElectricWaterUsage> ListElecWaterOfRoomIdInMonth(int month,int year,Long roomId);
    @Query(value = "select ewu.* from resident_history as reh inner join slot on reh.slot_id = slot.id  \n" +
            "                       inner join room on slot.room_id = room.id inner join electric_water_usage as ewu on room.id = ewu.room_id\n" +
            "                       where resident_id=? and (ewu.month_pay BETWEEN reh.start_date and reh.end_date) and reh.checkin_date IS NOT NULL\n" +
            "            AND((ewu.month_pay >= reh.checkin_date AND DATEADD(month, DATEDIFF(month, 0, ewu.month_pay), 0) <= reh.end_date AND reh.checkout_date IS NULL)\n" +
            "            OR(ewu.month_pay >= reh.checkin_date AND DATEADD(month, DATEDIFF(month, 0, ewu.month_pay), 0)<= reh.checkout_date AND reh.checkout_date IS NOT NULl\n" +
            "            ))",nativeQuery = true)
    List<ElectricWaterUsage> ListElecWaterOfResidenId(Long ResidentId) ;
    @Query (value="SELECT TOP 1 * FROM electric_water_usage\n" +
            "WHERE room_id = ? ORDER BY id DESC",nativeQuery = true)
    ElectricWaterUsage EndNumberELectricWater(@Param("roomid") Long roomId);
}

