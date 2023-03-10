package us.thedorm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.CheckInOutRepository;
import us.thedorm.repositories.ResidentHistoryRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/check")
public class CheckInOutController {
    @Autowired
    ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private CheckInOutRepository checkInOutRepository;
    @GetMapping("")

    ResponseEntity<ResponseObject> getAllCheckInOut() {
        List<CheckInOut> found = checkInOutRepository.findAll();
        if (found.size() == 0) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", found)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<CheckInOut> found = checkInOutRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("")
    ResponseEntity<ResponseObject> insertRoom(@RequestBody CheckInOut checkInOut) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", checkInOutRepository.save(checkInOut))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody CheckInOut newCheckInOut, @PathVariable Long id) {

      CheckInOut update = checkInOutRepository.findById(id)
                .map(check_InOut -> {
                    check_InOut.setGuard(newCheckInOut.getGuard());
                    check_InOut.setResident(newCheckInOut.getResident());
                    check_InOut.setCreateDate(newCheckInOut.getCreateDate());
                    check_InOut.setConfirmDate(newCheckInOut.getConfirmDate());
                    check_InOut.setType(newCheckInOut.getType());
                    return checkInOutRepository.save(check_InOut);
                }).orElseGet(() -> null);
        if(update != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCheckInOut(@PathVariable Long id) {

        boolean exists = checkInOutRepository.existsById(id);
        if (exists) {
           checkInOutRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }


//    @PostMapping("/check-in")
//    ResponseEntity<ResponseObject> checkIsCheckIn(@RequestBody UserInfo user) {
//        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());
//
//        if (residentHistory.get().getCheckinDate() == null) {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("OK", "", false)
//            );
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new ResponseObject("failed", "", true)
//        );
//
//    }

}
