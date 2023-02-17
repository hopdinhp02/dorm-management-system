package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
