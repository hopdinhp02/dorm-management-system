package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
