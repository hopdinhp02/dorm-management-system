package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Room;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsById(Long roomId);
    List<Room> getRoomsByDorm_Id(Long dormId);

    List<Room> getRoomsByDorm_IdAndSex(long dormId,boolean gender);
    @Query(value = "select * from room where dorm_id = :dorm_id and room.id NOT IN( select distinct room.id from room inner join dorm on room.dorm_id = dorm.id \n" +
            "left join electric_water_usage as ewu on room.id = ewu.room_id where dorm.id=:dorm_id " +
            "and (:month = MONTH(ewu.month_pay) and(:year = YEAR(ewu.month_pay))) )", nativeQuery = true)
    List<Room> ListRoomNotEvenRecordElectricWaterUsageOfDormId(@Param("dorm_id") Long dormId,@Param("month")  String month,@Param("year") String year);
}
