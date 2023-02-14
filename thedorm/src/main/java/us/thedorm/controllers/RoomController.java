package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.branch;
import us.thedorm.models.dorm;
import us.thedorm.models.room;
import us.thedorm.repositories.BranchRepository;
import us.thedorm.repositories.RoomRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllRoom() {
        List<room> foundRooms = roomRepository.findAll();
        if (foundRooms.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundRooms)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<room> foundRoom = roomRepository.findById(id);
        return foundRoom.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundRoom)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody room newRoom) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", roomRepository.save(newRoom))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateRoom(@RequestBody room newRoom, @PathVariable Long id) {
        room updateRoom = roomRepository.findById(id)
                .map(room -> {
                   room.setName(newRoom.getName());
                   room.setNo_beds(newRoom.getNo_beds());
                   room.setFloor(newRoom.getFloor());
                   room.setDorms(newRoom.getDorms());
                   room.setBeds(newRoom.getBeds());
                   room.setBasePrice(newRoom.getBasePrice());
                   room.setStatus(newRoom.getStatus());

                    return roomRepository.save(room);
                }).orElseGet(() -> roomRepository.save(newRoom));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert Product successfully", updateRoom)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = roomRepository.existsById(id);
        if (exists) {
            roomRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }
}
