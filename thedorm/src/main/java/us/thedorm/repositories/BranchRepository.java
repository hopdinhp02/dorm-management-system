package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.branch;

public interface BranchRepository extends JpaRepository<branch, Long> {

}
