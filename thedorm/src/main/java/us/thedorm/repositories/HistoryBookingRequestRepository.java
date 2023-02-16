package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.history_booking_request;

public interface HistoryBookingRequestRepository extends JpaRepository<history_booking_request, Long> {
}
