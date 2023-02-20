package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Dorm;
import us.thedorm.repositories.DormRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/dorms")
public class DormController {
    @Autowired
    private DormRepository dormRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllDorms() {
        List<Dorm> foundDorms = dormRepository.findAll();
        if(foundDorms.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundDorms)
        );

    }
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Dorm> foundDorm = dormRepository.findById(id);
        return foundDorm.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundDorm)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }
    @GetMapping("/branch/{id}")
    ResponseEntity<ResponseObject> findByBranchId(@PathVariable Long id) {
        List<Dorm> foundDorms = dormRepository.getDormsByBranch_Id(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundDorms));

    }


    @PostMapping("")
    ResponseEntity<ResponseObject> insertDorm(@RequestBody Dorm newDorm){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", dormRepository.save(newDorm))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateDorm(@RequestBody Dorm newDorm, @PathVariable Long id){
        Dorm updateDorm = dormRepository.findById(id)
                .map(dorm -> {

                    dorm.setName(newDorm.getName());
                    dorm.setBranch(newDorm.getBranch());

                    dorm.setRooms(newDorm.getRooms());
                    return dormRepository.save(dorm);

                }).orElseGet(()-> null);
        if(updateDorm !=null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateDorm)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );


    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteDorm(@PathVariable Long id){
        boolean exists = dormRepository.existsById(id);
        if(exists){
            dormRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

}
