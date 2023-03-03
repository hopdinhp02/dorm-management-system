package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.Billing;
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
    List<ResidentHistory> findByDateBetweenStartDateAndEndDate(@Param("date")Date date);

    @Query(value = "select * from resident_history where slot_id = :slot_id and (:date between start_date and end_date)", nativeQuery = true)
    ResidentHistory findBySlotIdWithDateBetweenStartDateAndEndDate(@Param("slot_id")long id,@Param("date") Date date);

    List<ResidentHistory> findBySlot_IdAAndCheckinDateIsNull(Long slotId);

    List<ResidentHistory> findBySlot_IdAndCheckoutDateIsNull(Long slotId);
}
