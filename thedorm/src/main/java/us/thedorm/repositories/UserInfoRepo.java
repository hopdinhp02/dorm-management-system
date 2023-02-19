package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.UserInfo;


import java.util.Optional;


public interface UserInfoRepo extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUsername(String username);

}
