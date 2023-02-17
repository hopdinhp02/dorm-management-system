package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.HistoryBookingRequest;

public interface HistoryBookingRequestRepository extends JpaRepository<HistoryBookingRequest, Long> {
}
