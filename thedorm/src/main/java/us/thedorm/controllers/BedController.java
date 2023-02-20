package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Bed;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Room;
import us.thedorm.repositories.BedRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/beds")
public class BedController {
    @Autowired
    private BedRepository bedRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllBeds() {
        List<Bed> foundBeds = bedRepository.findAll();
        if (foundBeds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("empty", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBeds)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Bed> foundBed = bedRepository.findById(id);
        return foundBed.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBed)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @GetMapping("/room/{id}")
    ResponseEntity<ResponseObject> findByRoomId(@PathVariable Long id) {
        List<Bed> foundBeds = bedRepository.getBedsByRoom_Id(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBeds));
    }
    @PostMapping("")
    ResponseEntity<ResponseObject> insertBed(@RequestBody Bed newBed) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bedRepository.save(newBed))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateBed(@RequestBody Bed newBed, @PathVariable Long id) {
        Bed updateBed = bedRepository.findById(id)
                .map(bed -> {
                    bed.setName(newBed.getName());
                    bed.setRoom(newBed.getRoom());
                    bed.setStatus(newBed.getStatus());
                    bed.setDormFacilities(newBed.getDormFacilities());
                    bed.setResidentHistories(newBed.getResidentHistories());
                    return bedRepository.save(bed);

                }).orElseGet(() -> null);
        if(updateBed != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateBed)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteBed(@PathVariable Long id) {
        boolean exists = bedRepository.existsById(id);
        if (exists) {
            bedRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
