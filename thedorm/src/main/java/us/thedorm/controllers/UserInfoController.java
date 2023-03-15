package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Billing;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Slot;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.repositories.UserInfoRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/user-infos")
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private BillingRepository billingRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUserInfo() {
        List<UserInfo> foundUserInfo = userInfoRepository.findAll();
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
        Optional<UserInfo> foundUserInfo = userInfoRepository.findById(id);
        return foundUserInfo.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundUserInfo)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("")
    ResponseEntity<ResponseObject> insertUserInfo(@RequestBody UserInfo newUserInfo) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", userInfoRepository.save(newUserInfo))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateUserInfo(@RequestBody UserInfo newUserInfo, @PathVariable Long id) {

        UserInfo updateUserInfo = userInfoRepository.findById(id)
                .map(userInfo -> {
                    userInfo.setUsername(newUserInfo.getUsername());
                    userInfo.setPassword(newUserInfo.getPassword());
                    userInfo.setName(newUserInfo.getName());
                    userInfo.setEmail(newUserInfo.getEmail());
                    userInfo.setPhone(newUserInfo.getPhone());
                    return userInfoRepository.save(userInfo);
                }).orElseGet(() -> null);

        if (updateUserInfo != null) {
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

        boolean exists = userInfoRepository.existsById(id);
        if (exists) {
            userInfoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @PutMapping("/{id}/topup")
    ResponseEntity<ResponseObject> topUp(@RequestBody UserInfo newUserInfo, @PathVariable Long id) {
        UserInfo TopUp = userInfoRepository.findById(id)
                .map(userInfo -> {
                    userInfo.setBalance(userInfo.getBalance() + newUserInfo.getBalance());

                    return userInfoRepository.save(userInfo);
                }).orElseGet(() -> null);
// nếu giá trị top up trả về khác null ( tức là đã cập nhật balance )
        if (TopUp != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Top up successfully", TopUp)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @GetMapping("/check-balance")
    ResponseEntity<ResponseObject> checkBalanceForBookingRequest(@RequestBody Slot bookSlot) {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Slot> slot = slotRepository.findById(bookSlot.getId());
        if (slot.isPresent()) {
            int cost = slot.get().getRoom().getBasePrice().getSlotPrice();

            if (user.getBalance() < cost) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "Can't Book", false)
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "OK", true)
            );

        }
        return null;
    }
    @PostMapping("/check-balane-to-pay-bill")
    ResponseEntity<ResponseObject> checkBalanceToPayBills(@RequestBody Billing bill){
       UserInfo user =(UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Optional<Billing> billing = billingRepository.findById(bill.getId());
       if(billing.isPresent()){
           int cost = billing.get().getCost();
           if(user.getBalance()< cost){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                 new ResponseObject("Fail","Can not pay bills",false)
               );
           }
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK","YOU CAN PAY BILL",true));
       }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Fail","NOT FOUND",""));
    }
    @PutMapping("/pay-bill")
    ResponseEntity<ResponseObject> payBills(@RequestBody Billing bill){
        UserInfo user =(UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Billing> billing = billingRepository.findById(bill.getId());
        if(billing.isPresent()){
            int cost = billing.get().getCost();
            if(user.getBalance()< cost){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("Fail","Can not pay bills","")
                );
            }else{
                user.setBalance(user.getBalance()-cost);
                billing.get().setStatus(Billing.Status.Paid);
                userInfoRepository.save(user);
                billingRepository.save(billing.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK","YOU PAID ",billing.get()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Fail","NOT FOUND",""));
    }
    }

