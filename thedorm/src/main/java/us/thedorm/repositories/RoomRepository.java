package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import us.thedorm.models.Room;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsById(Long roomId);
    List<Room> getRoomsByDorm_Id(Long dormId);
    @Query(value = "select * from room where dorm_id = 1 and room.id NOT IN( select distinct room.id from room inner join dorm on room.dorm_id = dorm.id \n" +
            "left join electric_water_usage as ewu on room.id = ewu.room_id where dorm.id=? " +
            "and (? = MONTH(ewu.month_pay) and(? = YEAR(ewu.month_pay))) )", nativeQuery = true)
    List<Room> ListRoomNotEvenRecordElectricWaterUsageOfDormId(Long dormId,String month,String year);
}
