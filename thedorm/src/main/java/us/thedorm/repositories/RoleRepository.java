package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Role;


public interface RoleRepository extends JpaRepository<Role,Long> {
}
