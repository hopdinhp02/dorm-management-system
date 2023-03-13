package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.UserInfo;
import us.thedorm.models.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
}
