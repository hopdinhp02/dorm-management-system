package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;

import java.util.Date;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
}
