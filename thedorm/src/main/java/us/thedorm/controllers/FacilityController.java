package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;

import us.thedorm.repositories.FacilityDetailRepository;
import us.thedorm.repositories.FacilityHistoryRepository;
import us.thedorm.repositories.FacilityRepository;
import us.thedorm.repositories.MaintenanceRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/facilities")
public class FacilityController {
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private FacilityDetailRepository facilityDetailRepository;
    @Autowired
    private FacilityHistoryRepository facilityHistoryRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<Facility> founds = facilityRepository.findAll();
        if(founds.size() == 0){
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
        Optional<Facility> found = facilityRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody Facility newFacility){
        facilityDetailRepository.save(newFacility.getFacilityDetail());
        Facility facility = facilityRepository.save(newFacility);
        FacilityHistory facilityHistory = FacilityHistory.builder()
                .facility(facility)
                .slot(facility.getSlot())
                .room(facility.getRoom())
                .dorm(facility.getDorm())
                .branch(facility.getBranch())
                .changeDate(new Date())
                .build();
        facilityHistoryRepository.save(facilityHistory);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", facility)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateLocation(@RequestBody Facility newFacility, @PathVariable Long id){
        Facility updateFacility = facilityRepository.findById(id)
                .map(facility -> {
                    if(newFacility.getSlot() != null){
                        facility.setSlot(newFacility.getSlot());
                        facility.setRoom(null);
                        facility.setDorm(null);
                        facility.setBranch(null);
                    } else if (newFacility.getRoom() != null) {
                        facility.setRoom(newFacility.getRoom());
                        facility.setSlot(null);
                        facility.setDorm(null);
                        facility.setBranch(null);
                    } else if (newFacility.getDorm() != null) {
                        facility.setDorm(newFacility.getDorm());
                        facility.setSlot(null);
                        facility.setRoom(null);
                        facility.setBranch(null);
                    } else if (newFacility.getBranch() != null) {
                        facility.setBranch(newFacility.getBranch());
                        facility.setSlot(null);
                        facility.setRoom(null);
                        facility.setDorm(null);
                    }
                    return facilityRepository.save(facility);

                }).orElseGet(()-> null);
        if(updateFacility !=null){
            FacilityHistory facilityHistory = FacilityHistory.builder()
                    .facility(updateFacility)
                    .slot(updateFacility.getSlot())
                    .room(updateFacility.getRoom())
                    .dorm(updateFacility.getDorm())
                    .branch(updateFacility.getBranch())
                    .changeDate(new Date())
                    .build();
            facilityHistoryRepository.save(facilityHistory);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", updateFacility)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id){
        boolean exists = facilityRepository.existsById(id);
        if(exists){
            facilityRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
    @PutMapping("/{id}/facility-detail")
    ResponseEntity<ResponseObject> updateFacilityDetail(@RequestBody Facility newFacility, @PathVariable Long id){
        Facility updateFacilityDetail = facilityRepository.findById(id)
                .map(facility -> {
                    facility.setFacilityDetail(newFacility.getFacilityDetail());
                    return facilityRepository.save(facility);

                }).orElseGet(()-> null);
        if(updateFacilityDetail !=null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", updateFacilityDetail)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @GetMapping("/{id}/facility-detail/maintenances")
    ResponseEntity<ResponseObject> getAllMaintenancesByFacilityId(@PathVariable Long id) {
        Optional<Facility> facility = facilityRepository.findById(id);
        if(facility.isPresent()){
            List<Maintenance> maintenances = (List<Maintenance>) facility.get().getFacilityDetail().getMaintenances();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", maintenances)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @GetMapping("/facility-detail/maintenances")
    ResponseEntity<ResponseObject> getAllMaintenances(@PathVariable Long id) {
        List<Maintenance> founds = maintenanceRepository.findAll();
        if(founds.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );
    }
    @PostMapping("/{id}/facility-detail/maintenances")
    ResponseEntity<ResponseObject> maintenance(@RequestBody Maintenance maintenance, @PathVariable Long id){
        Facility updateFacilityDetail = facilityRepository.findById(id)
                .map(facility -> {
                    facility.getFacilityDetail().getMaintenances().add(maintenance);
                    return facilityRepository.save(facility);
                }).orElseGet(()-> null);
        if(updateFacilityDetail !=null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", maintenance)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
    @GetMapping("/falicility-histories")
    ResponseEntity<ResponseObject> getAllHistory() {
        List<FacilityHistory> founds = facilityHistoryRepository.findAll();
        if(founds.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/{id}/falicility-histories")
    ResponseEntity<ResponseObject> getAllHistory(@PathVariable Long id) {
        Optional<Facility> found = facilityRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found.get().getFacilityHistories())
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));

    }








}