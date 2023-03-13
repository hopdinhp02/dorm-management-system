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
        Optional<Facility> found = facilityRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @GetMapping("/facility-detail/{id}")
    ResponseEntity<ResponseObject> findByFacilityDetailId(@PathVariable Long id) {
        Optional<FacilityDetail> found = facilityDetailRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody Facility newFacility, @RequestParam(name = "quantity", required = false) String quantityRaw) {
        int quantity;
        if (quantityRaw == null) {
            quantity = 1;
        } else {
            try {
                quantity = Integer.parseInt(quantityRaw);
            } catch (NumberFormatException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("false", "quantity is a number!", ""
                        ));
            }
        }

        FacilityDetail facilityDetail = newFacility.getFacilityDetail();
        for (int i = 0; i < quantity; i++) {
            FacilityDetail newFD = FacilityDetail.builder()
                    .name(facilityDetail.getName())
                    .provider(facilityDetail.getProvider())
                    .producingDate(facilityDetail.getProducingDate())
                    .expirationDate(facilityDetail.getExpirationDate())
                    .price(facilityDetail.getPrice())
                    .value(facilityDetail.getValue())
                    .codeProduct(facilityDetail.getCodeProduct())
                    .type(facilityDetail.getType())
                    .status(FacilityDetail.Status.good)
                    .build();

            facilityDetailRepository.save(newFD);
            Facility facility = Facility.builder()
                    .facilityDetail(newFD)
                    .build();
            facilityRepository.save(facility);
            FacilityHistory facilityHistory = FacilityHistory.builder()
                    .facility(facility)
                    .slot(facility.getSlot())
                    .room(facility.getRoom())
                    .dorm(facility.getDorm())
                    .branch(facility.getBranch())
                    .startDate(new Date())
                    .build();
            facilityHistoryRepository.save(facilityHistory);

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", "")
        );

    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateLocation(@RequestBody Facility newFacility, @PathVariable Long id) {
        Facility updateFacility = facilityRepository.findById(id)
                .map(facility -> {
                    if (newFacility.getSlot() != null) {
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

                }).orElseGet(() -> null);
        Optional<FacilityHistory> oldFH = facilityHistoryRepository.findTopByFacility_IdOrderByIdDesc(id);
        if(oldFH.isPresent()){
            oldFH.get().setEndDate(new Date());
        }
        if (updateFacility != null) {
            FacilityHistory facilityHistory = FacilityHistory.builder()
                    .facility(updateFacility)
                    .slot(updateFacility.getSlot())
                    .room(updateFacility.getRoom())
                    .dorm(updateFacility.getDorm())
                    .branch(updateFacility.getBranch())
                    .startDate(new Date())
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

    @PutMapping("/facility-detail/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody FacilityDetail newFacilityDetail, @PathVariable Long id) {
        FacilityDetail updateFacilityDetail = facilityDetailRepository.findById(id).orElseGet(() -> null);

        if (updateFacilityDetail != null) {
            newFacilityDetail.setId(id);
            updateFacilityDetail =  facilityDetailRepository.save(newFacilityDetail);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", updateFacilityDetail)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        boolean exists = facilityRepository.existsById(id);
        if (exists) {
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
    ResponseEntity<ResponseObject> updateFacilityDetailStatus(@RequestBody Facility newFacility, @PathVariable Long id) {
        Facility updateFacilityDetail = facilityRepository.findById(id)
                .map(facility -> {
                    facility.getFacilityDetail().setStatus(newFacility.getFacilityDetail().getStatus());
                    return facilityRepository.save(facility);

                }).orElseGet(() -> null);
        if (updateFacilityDetail != null) {
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
        if (facility.isPresent()) {
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
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );
    }

    @PostMapping("/{id}/facility-detail/maintenances")
    //facility
    ResponseEntity<ResponseObject> maintenance(@RequestBody Maintenance maintenance, @PathVariable Long id) {
        Facility updateFacilityDetail = facilityRepository.findById(id)
                .map(facility -> {
                    maintenance.setFacilityDetail(facility.getFacilityDetail());
                    facility.getFacilityDetail().getMaintenances().add(maintenance);
                    return facilityRepository.save(facility);
                }).orElseGet(() -> null);
        if (updateFacilityDetail != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Update successfully", maintenance)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @GetMapping("/facility-histories")
    ResponseEntity<ResponseObject> getAllHistory() {
        List<FacilityHistory> founds = facilityHistoryRepository.findAll();
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/{id}/facility-histories")
    ResponseEntity<ResponseObject> getAllHistory(@PathVariable Long id) {
        Optional<Facility> found = facilityRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found.get().getFacilityHistories())
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));

    }

    @GetMapping("/slots/{id}")
    ResponseEntity<ResponseObject> getFacilityBySlot(@PathVariable Long id) {
        List<Facility> founds = facilityRepository.findBySlot_Id(id);
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/rooms/{id}")
    ResponseEntity<ResponseObject> getFacilityByRoom(@PathVariable Long id) {
        List<Facility> founds = facilityRepository.findByRoom_Id(id);
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/dorms/{id}")
    ResponseEntity<ResponseObject> getFacilityByDorm(@PathVariable Long id) {
        List<Facility> founds = facilityRepository.findByDorm_Id(id);
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );

    }

    @GetMapping("/branchs/{id}")
    ResponseEntity<ResponseObject> getFacilityByBranch(@PathVariable Long id) {
        List<Facility> founds = facilityRepository.findByBranch_Id(id);
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );
    }

    @GetMapping("not-assign")
    ResponseEntity<ResponseObject> getNotAssignFacility() {
        List<Facility> founds = facilityRepository.findNotAssignFacility();
        if (founds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", founds)
        );
    }



}
