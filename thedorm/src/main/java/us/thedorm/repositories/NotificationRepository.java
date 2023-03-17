package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Billing;
import us.thedorm.models.Notification;
import us.thedorm.models.Permission;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long>
{
    List<Notification> findAllByUserInfo_Id(long id);
}
