package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.BookingRequest;

public interface BookingRequestRepository extends JpaRepository<BookingRequest,Long> {
}
