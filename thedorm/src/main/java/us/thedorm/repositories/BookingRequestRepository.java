package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.*;

import java.util.List;
import java.util.Optional;

public interface BookingRequestRepository extends JpaRepository<BookingRequest,Long> {

     Optional<BookingRequest> findTopByUserInfo_IdAndStatusIsNotOrderByIdDesc(long id, BookingRequest.Status status);
}
