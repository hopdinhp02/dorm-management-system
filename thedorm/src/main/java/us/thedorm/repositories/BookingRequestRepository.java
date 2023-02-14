package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.booking_request;

public interface BookingRequestRepository extends JpaRepository<booking_request,Long> {
}
