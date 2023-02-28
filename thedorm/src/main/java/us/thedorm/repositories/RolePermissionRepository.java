package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {
}
