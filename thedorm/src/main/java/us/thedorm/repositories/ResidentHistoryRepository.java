package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;

import java.util.Date;

@Repository
@Transactional
public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
    @Modifying
    @Query(value = "insert into resident_history(resident_id,bed_id,checkin_date,checkout_date) values(?1,?2,?3,?4);", nativeQuery = true)
    void insertResidentHistory(long resident_id, long bed_id, Date checkin_date, Date checkout_date);
}
