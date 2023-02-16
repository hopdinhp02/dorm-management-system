package us.thedorm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.thedorm.repositories.BranchRepository;

@SpringBootApplication
public class ThedormApplication {
	public static void main(String[] args) {
		SpringApplication.run(ThedormApplication.class, args);
	}



}
