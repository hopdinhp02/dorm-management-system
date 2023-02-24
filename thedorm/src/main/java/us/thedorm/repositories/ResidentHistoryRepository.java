package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.ResidentHistory;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
}
