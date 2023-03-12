package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.UserInfo;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.repositories.UserInfoRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/resident-histories")
public class ResidentHistoryController {
    @Autowired
    ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    UserInfoRepository userInfoRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<ResidentHistory> founds = residentHistoryRepository.findAll();
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<ResidentHistory> found = residentHistoryRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody ResidentHistory newResidentHistory) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", residentHistoryRepository.save(newResidentHistory))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody ResidentHistory newResidentHistory, @PathVariable Long id) {
        ResidentHistory updateHistory = residentHistoryRepository.findById(id)
                .map(history -> {

                    history.setCheckinDate(newResidentHistory.getCheckinDate());
                    history.setCheckoutDate(newResidentHistory.getCheckoutDate());
                    history.setStartDate(newResidentHistory.getStartDate());
                    history.setEndDate(newResidentHistory.getEndDate());
                    history.setUserInfo(newResidentHistory.getUserInfo());
                    history.setSlot(newResidentHistory.getSlot());
                    return residentHistoryRepository.save(history);

                }).orElseGet(() -> null);
        if (updateHistory != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateHistory)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        boolean exists = residentHistoryRepository.existsById(id);
        if (exists) {
            residentHistoryRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @PostMapping("/guard/check-in")
    ResponseEntity<ResponseObject> checkIn(@RequestBody UserInfo resident) {

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(resident.getId());
        if (residentHistory.isPresent()) {
            residentHistory.get().setCheckinDate(new Date());
            residentHistoryRepository.save(residentHistory.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "checked", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", "")
        );
    }

    @PostMapping("/is-check-in")
    ResponseEntity<ResponseObject> checkIsCheckIn(@RequestBody UserInfo user) {
        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());

        if (residentHistory.get().getCheckinDate() == null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Failed", "", false)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Ok", "", true)
        );

    }


    @PostMapping("/guard/check-out")
    ResponseEntity<ResponseObject> checkOut(@RequestBody UserInfo resident) {

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(resident.getId());
        if (residentHistory.isPresent()) {
            if (checkIsCheckIn(resident).getBody().getData().equals(true)) {
                residentHistory.get().setCheckoutDate(new Date());
                residentHistoryRepository.save(residentHistory.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "checked", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Fail", "Resident do not check-in", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Fail", "Not Found", "")
        );
    }

    @GetMapping("/guard/check-in/slots/{id}")
    ResponseEntity<ResponseObject> ViewNotCheckInYetBySlot(@PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findBySlot_IdAndCheckinDateIsNull(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", residentHistories)
        );
    }

    @GetMapping("/guard/check-out/slots/{id}")
    ResponseEntity<ResponseObject> ViewNotCheckOutYetBySlot(@PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findBySlot_IdAndCheckoutDateIsNull(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", residentHistories)
        );
    }

    @GetMapping("/residents/current-slot")
    ResponseEntity<ResponseObject> getCurrentSlotOfResident() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findCurrentSlotOfResident(user.getId());
        if (residentHistory.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", residentHistory)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Fail", "Not Found", "")
        );

    }


    @GetMapping("/find/{name}")
    ResponseEntity<ResponseObject> searchResidentByName(@PathVariable String name) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findResidentHistoriesByName(name);

        return  checkForSearch(residentHistories);
    }

    @GetMapping("/find/{name}/slot/{id}")
    ResponseEntity<ResponseObject> searchByNameAndSlotId(@PathVariable String name, @PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findResidentHistoriesByNameAndSlotId(name, id);
      return checkForSearch(residentHistories);
    }


    @GetMapping("/find/{name}/room/{id}")
    ResponseEntity<ResponseObject> searchByNameAndRoomId(@PathVariable String name, @PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findResidentHistoriesByNameAndRoomId(name, id);

       return checkForSearch(residentHistories);
    }

    @GetMapping("/find/{name}/dorm/{id}")
    ResponseEntity<ResponseObject> searchByNameAndDormId(@PathVariable String name, @PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findResidentHistoriesByNameAndDormId(name, id);

    return  checkForSearch(residentHistories);
    }


    @GetMapping("/find/{name}/branch/{id}")
    ResponseEntity<ResponseObject> searchByNameAndBranchId(@PathVariable String name, @PathVariable Long id) {

        List<ResidentHistory> residentHistories = residentHistoryRepository.findResidentHistoriesByNameAndBranchId(name, id);

        return  checkForSearch(residentHistories);
    }


    ResponseEntity<ResponseObject> checkForSearch(List<ResidentHistory> residentHistories) {

        for (ResidentHistory item : residentHistories) {
            if (item.getCheckinDate() == null || item.getCheckoutDate() != null) {
                residentHistories.remove(item);
            }

            if (residentHistories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "No found Resident", "")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", residentHistories)
        );
    }
}


