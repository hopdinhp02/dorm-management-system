package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import us.thedorm.models.UserInfo;


import java.util.List;
import java.util.Optional;


public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUsername(String username);
    Optional<UserInfo> findById(long id);


}
