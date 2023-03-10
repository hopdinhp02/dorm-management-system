package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Billing;
import us.thedorm.models.Maintenance;
import us.thedorm.models.ResponseObject;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.MaintenanceRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/maintenance")
public class MaintenanceController {
    @Autowired
    public MaintenanceRepository maintenanceRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<Maintenance> founds = maintenanceRepository.findAll();
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
        Optional<Maintenance> found = maintenanceRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody Maintenance newMaintenance) {
        newMaintenance.setFixDate(new Date());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", maintenanceRepository.save(newMaintenance))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody Maintenance newMaintenance, @PathVariable Long id) {
        Maintenance update = maintenanceRepository.findById(id)
                .map(maintenance -> {
                    maintenance.setPrice(newMaintenance.getPrice());
                    maintenance.setNote(newMaintenance.getNote());
                    maintenance.setFixDate(newMaintenance.getFixDate());
                    return maintenanceRepository.save(maintenance);
                }).orElseGet(() -> null);

        if(update != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update Product successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        boolean exists = maintenanceRepository.existsById(id);
        if (exists) {
            maintenanceRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
