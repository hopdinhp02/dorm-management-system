package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import us.thedorm.models.ElectricWaterUsage;
import us.thedorm.models.UserInfo;


import java.util.List;
import java.util.Optional;


public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUsername(String username);
    Optional<UserInfo> findById(long id);
    @Query(value = " SELECT * from user_info where name like :keyword and role like :role and user_info.is_active like :isActive",nativeQuery = true)
    List<UserInfo> SearchUsers(@Param("keyword") String keyword,@Param("role") String role,@Param("isActive")String isActive );



}
