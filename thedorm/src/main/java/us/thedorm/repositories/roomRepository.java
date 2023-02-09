package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.dorm;
import us.thedorm.models.room;

public interface roomRepository  extends JpaRepository<room, Long> {
}
