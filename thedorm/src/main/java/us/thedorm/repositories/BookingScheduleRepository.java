package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.BookingSchedule;

import java.util.List;
import java.util.Optional;

public interface BookingScheduleRepository extends JpaRepository<BookingSchedule,Long> {

    Optional<BookingSchedule> findTopByBranch_IdOrderByIdDesc(long id);

    @Query(value =
            "   select top 2 *From [booking_schedule] where [booking_schedule].branch_id =:branch_id  order by id desc", nativeQuery = true)
    List<BookingSchedule> findBookingScheduleLastSemesterByBranch(@Param("branch_id") long id);
}