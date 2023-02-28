package us.thedorm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.UserInfo;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface  ResidentRepository extends JpaRepository<ResidentHistory, Long> {


}
