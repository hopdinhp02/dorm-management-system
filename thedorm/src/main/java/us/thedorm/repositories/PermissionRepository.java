package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Permission;
import us.thedorm.models.UserInfo;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
