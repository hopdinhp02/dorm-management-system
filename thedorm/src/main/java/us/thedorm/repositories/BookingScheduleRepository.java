package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.BookingSchedule;

import java.util.Optional;

public interface BookingScheduleRepository extends JpaRepository<BookingSchedule,Long> {

    Optional<BookingSchedule> findBookingScheduleByBranch_Id(long id);
}
