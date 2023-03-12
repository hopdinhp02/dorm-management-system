package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Slot;


import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> getSlotsByRoom_Id(Long roomId);

    List<Slot> getSlotsByRoom_IdAndStatus(long id, Slot.Status status);

    List<Slot> findAllByRoom_Dorm_BranchId(long id);
}
