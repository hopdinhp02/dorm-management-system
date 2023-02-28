package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.CheckInOut;

public interface CheckInOutRepository extends JpaRepository<CheckInOut,Long> {

}
