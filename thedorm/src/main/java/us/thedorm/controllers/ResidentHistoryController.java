package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.ResponseObject;
import us.thedorm.repositories.ResidentHistoryRepository;


import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/resident-histories")
public class ResidentHistoryController {
    @Autowired
    ResidentHistoryRepository residentHistoryRepository;

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
                    history.setBed(newResidentHistory.getBed());
                    history.setUserInfo(newResidentHistory.getUserInfo());
                    history.setCheckinDate(newResidentHistory.getCheckinDate());
                    history.setCheckoutDate(newResidentHistory.getCheckoutDate());
                    history.setStartDate(newResidentHistory.getStartDate());
                    history.setEndDate(newResidentHistory.getEndDate());
                    return residentHistoryRepository.save(history);

                }).orElseGet(() -> null);
        if(updateHistory != null){
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
}
