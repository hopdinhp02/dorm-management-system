package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.branch;
import us.thedorm.models.user_info;
import us.thedorm.repositories.UserInfoRepo;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/user_info")
public class UserInfoController {
    @Autowired
    private UserInfoRepo userInfoRepo;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUserInfo() {
     List<user_info> foundUserInfo = userInfoRepo.findAll();
        if (foundUserInfo.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundUserInfo)
        );

    }


    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<user_info> foundUserInfo  = userInfoRepo.findById(id);
        return foundUserInfo.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundUserInfo)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("/insert")

    ResponseEntity<ResponseObject> insertUserInfo(@RequestBody user_info newUserInfo) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", userInfoRepo.save(newUserInfo))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateUserInfo(@RequestBody user_info newUserInfo, @PathVariable Long id) {

        user_info updateUserInfo = userInfoRepo.findById(id)
                .map(user_info -> {
                   user_info.setUsername(newUserInfo.getUsername());
                    user_info.setPassword(newUserInfo.getPassword());
                    user_info.setName(newUserInfo.getName());
                    user_info.setEmail(newUserInfo.getEmail());
                    user_info.setPhone(newUserInfo.getPhone());
                    return userInfoRepo.save(user_info);
                }).orElseGet(() -> userInfoRepo.save(newUserInfo));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update successfully", updateUserInfo)
        );
    }

    @DeleteMapping("/{id}")

    ResponseEntity<ResponseObject> deleteUserInfo(@PathVariable Long id) {

        boolean exists = userInfoRepo.existsById(id);
        if (exists) {
           userInfoRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }



}
