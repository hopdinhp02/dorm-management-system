package us.thedorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {
}
