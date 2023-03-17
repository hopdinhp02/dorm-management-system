package us.thedorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thedorm.models.Notification;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.NotificationRepository;

import java.util.Date;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    public Notification sendNotification(String title, String content, Long userid){
        Notification newNotification = Notification.builder()
                .title(title)
                .content(content)
                .createdDate(new Date())
                .userInfo(UserInfo.builder().id(userid).build())
                .build();
        return notificationRepository.save(newNotification);
    }
}
