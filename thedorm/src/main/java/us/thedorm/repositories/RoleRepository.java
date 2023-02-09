package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.role;

public interface RoleRepository extends JpaRepository<role,Long> {
}
