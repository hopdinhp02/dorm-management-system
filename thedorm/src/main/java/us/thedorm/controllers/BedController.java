package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.bed;
import us.thedorm.models.branch;
import us.thedorm.repositories.BedRepository;
import us.thedorm.repositories.BranchRepository;

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
        List<bed> foundBeds = bedRepository.findAll();
        if (foundBeds.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBeds)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<bed> foundBed = bedRepository.findById(id);
        return foundBed.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBed)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBed(@RequestBody bed newBed) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bedRepository.save(newBed))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateBed(@RequestBody bed newBed, @PathVariable Long id) {
        bed updateBed = bedRepository.findById(id)
                .map(bed -> {
                    bed.setName(newBed.getName());
                    bed.setRooms(newBed.getRooms());
                    bed.setStatus(newBed.getStatus());
                    bed.setDormFacilities(newBed.getDormFacilities());
                    bed.setResidentHistories(newBed.getResidentHistories());
                    return bedRepository.save(bed);

                }).orElseGet(() -> bedRepository.save(newBed));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert Product successfully", updateBed)
        );
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
