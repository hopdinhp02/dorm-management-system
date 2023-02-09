package us.thedorm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.resident;



public interface  ResidentRepository extends JpaRepository<resident, Long> {


}
