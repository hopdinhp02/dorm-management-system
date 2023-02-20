package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Bed;
import us.thedorm.models.ElectricWaterUsage;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Room;
import us.thedorm.repositories.ElectricWaterUsageRepo;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/elec-water-usages")
public class ElectricWaterUsageController {
    @Autowired
    private ElectricWaterUsageRepo electricWaterUsageRepo;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUsage() {
        List<ElectricWaterUsage> found = electricWaterUsageRepo.findAll();
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
        Optional<ElectricWaterUsage> found =electricWaterUsageRepo.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody ElectricWaterUsage newElectricWaterUsage) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", electricWaterUsageRepo.save(newElectricWaterUsage))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody ElectricWaterUsage newElectricWaterUsage, @PathVariable Long id) {
        ElectricWaterUsage update = electricWaterUsageRepo.findById(id)
                .map(electricWaterUsage -> {
                    electricWaterUsage.setType(newElectricWaterUsage.getType());
                    electricWaterUsage.setFromDate(newElectricWaterUsage.getFromDate());
                    electricWaterUsage.setToDate(newElectricWaterUsage.getToDate());
                    electricWaterUsage.setFromAmount(newElectricWaterUsage.getFromAmount());
                    electricWaterUsage.setToAmount(newElectricWaterUsage.getToAmount());
                    electricWaterUsage.setRoom(newElectricWaterUsage.getRoom());
                    return electricWaterUsageRepo.save(electricWaterUsage);

                }).orElseGet(() -> null);
        if (update != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

        @DeleteMapping("/{id}")
        ResponseEntity<ResponseObject> delete(@PathVariable Long id) {

            boolean exists = electricWaterUsageRepo.existsById(id);
            if (exists) {
                electricWaterUsageRepo.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
    }



