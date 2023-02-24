package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Bed;


import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> getBedsByRoom_Id(Long roomId);

    List<Bed> getBedsByRoom_IdAndStatus(long id, Bed.Status status);
}
