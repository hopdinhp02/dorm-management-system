package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Bed;
import us.thedorm.models.BookingRequest;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.BedRepository;
import us.thedorm.repositories.UserInfoRepo;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/user-infos")
public class UserInfoController {
    @Autowired
    private UserInfoRepo userInfoRepo;
    @Autowired
    private BedRepository bedRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUserInfo() {
     List<UserInfo> foundUserInfo = userInfoRepo.findAll();
        if (foundUserInfo.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundUserInfo)
        );

    }


    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<UserInfo> foundUserInfo  = userInfoRepo.findById(id);
        return foundUserInfo.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundUserInfo)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("")

    ResponseEntity<ResponseObject> insertUserInfo(@RequestBody UserInfo newUserInfo) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", userInfoRepo.save(newUserInfo))
        );
    }

    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> updateUserInfo(@RequestBody UserInfo newUserInfo, @PathVariable Long id) {

        UserInfo updateUserInfo = userInfoRepo.findById(id)
                .map(userInfo -> {
                    userInfo.setUsername(newUserInfo.getUsername());
                    userInfo.setPassword(newUserInfo.getPassword());
                    userInfo.setName(newUserInfo.getName());
                    userInfo.setEmail(newUserInfo.getEmail());
                    userInfo.setPhone(newUserInfo.getPhone());
                    return userInfoRepo.save(userInfo);
                }).orElseGet(() -> null);

        if(updateUserInfo != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateUserInfo)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
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
    @PutMapping("/{id}/topup")
    ResponseEntity<ResponseObject> topUp(@RequestBody UserInfo newUserInfo, @PathVariable Long id){
        UserInfo TopUp = userInfoRepo.findById(id)
                .map(userInfo -> {
                        userInfo.setBalance(userInfo.getBalance()+newUserInfo.getBalance());

                    return userInfoRepo.save(userInfo);
                }).orElseGet(() -> null);

        if(TopUp != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", TopUp)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @GetMapping("/{id}/check-balance")
    ResponseEntity<ResponseObject> checkBalanceForBookingRequest(@PathVariable Long id) {
        UserInfo user =  (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Bed> bookBed = bedRepository.findById(id);
        if(bookBed.isPresent()) {
            int cost = bookBed.get().getRoom().getBasePrice().getBedPrice();

            if (user.getBalance() < cost) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "Can't Book", false)
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "OKK", true)
            );

        }

        return null;
    }



}
