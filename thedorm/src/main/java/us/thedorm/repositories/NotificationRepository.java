package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Notification;
import us.thedorm.models.Permission;

public interface NotificationRepository extends JpaRepository<Notification,Long>
{

}
