package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.BookingScheduleRepository;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.repositories.UserInfoRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;

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
    ResponseEntity<ResponseObject> topUpByUserId(@RequestBody UserInfo newUserInfo, @PathVariable Long id) {
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
    @PutMapping("/topup")
    ResponseEntity<ResponseObject> topUp(@RequestParam String amount) {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            user.setBalance(user.getBalance() + Double.parseDouble(amount));
            userInfoRepository.save(user);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Top up successfully", ""));

        }catch (NumberFormatException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", ex.getMessage(), "")
            );
        }


    }
    @GetMapping("/check-balance")
    ResponseEntity<ResponseObject> checkBalanceForBookingRequest(@RequestParam String slotid) {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            Long id = Long.parseLong(slotid);
            Optional<Slot> slot = slotRepository.findById(id);
            if (slot.isPresent()) {
                Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findTopByBranch_IdOrderByIdDesc(slot.get().getRoom().getDorm().getBranch().getId());
                if(bookingSchedule.isPresent()){
                    LocalDate startDate = bookingSchedule.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = bookingSchedule.get().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long monthsBetween = ChronoUnit.MONTHS.between(
                            startDate,
                            endDate);
                    long cost = slot.get().getRoom().getBasePrice().getSlotPrice() *  monthsBetween;
//                    System.out.println(monthsBetween);
//                    System.out.println(cost);
                    if (user.getBalance() < cost) {
                        return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("", "Can't Book", false)
                        );
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("OK", "OK", true)
                    );
                }


            }
        }catch (NumberFormatException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("", "Not Found", false)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("", "Not Found", false)
        );

    }
    @GetMapping("/booking-cost")
    ResponseEntity<ResponseObject> getBookingCost(@RequestParam String slotid) {
        try{
            Long id = Long.parseLong(slotid);
            Optional<Slot> slot = slotRepository.findById(id);
            if (slot.isPresent()) {
                Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findTopByBranch_IdOrderByIdDesc(slot.get().getRoom().getDorm().getBranch().getId());
                if(bookingSchedule.isPresent()){
                    LocalDate startDate = bookingSchedule.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = bookingSchedule.get().getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    long monthsBetween = ChronoUnit.MONTHS.between(
                            startDate.withDayOfMonth(1),
                            endDate.withDayOfMonth(1));
                    long cost = slot.get().getRoom().getBasePrice().getSlotPrice() *  monthsBetween;
//                    System.out.println(monthsBetween);
//                    System.out.println(cost);
                        return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("Ok", "", cost)
                        );
                    }
            }
        }catch (NumberFormatException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("", "Not Found", "")
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("", "Not Found", "")
        );

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

    @GetMapping("/search-users")
    List<UserInfo> searchUsers(@RequestParam String name,@RequestParam String role,@RequestParam String isActive){
return userInfoRepository.SearchUsers("%"+name+"%","%"+role+"%","%"+isActive+"%");
}


    @GetMapping("/balance")
    ResponseEntity<ResponseObject> getBalance() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", user.getBalance())
        );


    }
 }


