package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Billing;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.UserInfo;

import java.util.Date;
import java.util.Optional;


public interface ResidentHistoryRepository extends JpaRepository<ResidentHistory,Long> {
    Optional<ResidentHistory> findTopByUserInfo_IdOrderByEndDate(long id);
    Optional<ResidentHistory> findByUserInfo_IdOrderByEndDate(long id);

    Optional<ResidentHistory> findTopByUserInfo_IdOrderByIdDesc(long id);

}
