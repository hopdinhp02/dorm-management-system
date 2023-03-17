package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.MaintenanceRepository;
import us.thedorm.repositories.NotificationRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/notification")
public class NotificationController {
    @Autowired
    public NotificationRepository notificationRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<Notification> founds = notificationRepository.findAll();
        if(founds.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("empty", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );


    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Notification> found = notificationRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody Notification newNotification) {
        newNotification.setCreatedDate(new Date());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", notificationRepository.save(newNotification))

        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody Notification newNotification, @PathVariable Long id) {
        Notification update = notificationRepository.findById(id)
                .map(notification -> {
                    notification.setTitle(newNotification.getTitle());
                    notification.setContent(newNotification.getContent());
                    notification.setCreatedDate(new Date());
                    notification.setUserInfo(newNotification.getUserInfo());
                    return notificationRepository.save(notification);
                }).orElseGet(() -> null);

        if(update != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update Notification successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        boolean exists = notificationRepository.existsById(id);
        if (exists) {
            notificationRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @GetMapping("/resident")
    ResponseEntity<ResponseObject> getNotificationsByResidentId() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Notification> notifications = notificationRepository.findAllByUserInfo_Id(user.getId());
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "No found", "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", " successfully", notifications)
        );
    }
}
