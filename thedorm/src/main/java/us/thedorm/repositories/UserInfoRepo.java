package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.user_info;

public interface UserInfoRepo extends JpaRepository<user_info,Long> {
}
