package us.thedorm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.BookingScheduleRepository;
import us.thedorm.repositories.UserInfoRepository;

import java.util.Date;

@SpringBootApplication
public class ThedormApplication {


	public static void main(String[] args) {


		SpringApplication.run(ThedormApplication.class, args);
		Date date = new Date();
		System.out.println(date);
	}



}
