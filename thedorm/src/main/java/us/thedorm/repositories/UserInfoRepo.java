package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.UserInfo;


public interface UserInfoRepo extends JpaRepository<UserInfo,Long> {
}
