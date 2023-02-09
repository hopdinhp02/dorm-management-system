package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.room;

public interface RoomRepository extends JpaRepository<room, Long> {
}
