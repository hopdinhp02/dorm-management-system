package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Room;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.repositories.RoomRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SlotRepository slotRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllRoom() {
        List<Room> foundRooms = roomRepository.findAll();
        if (foundRooms.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundRooms)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Room> foundRoom = roomRepository.findById(id);
        return foundRoom.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundRoom)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @GetMapping("/dorm/{id}")
    ResponseEntity<ResponseObject> findByDormId(@PathVariable Long id) {
        List<Room> foundRooms = roomRepository.getRoomsByDorm_Id(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundRooms));

    }
    @PostMapping("")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Room newRoom) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", roomRepository.save(newRoom))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateRoom(@RequestBody Room newRoom, @PathVariable Long id) {
        Room updateRoom = roomRepository.findById(id)
                .map(room -> {
                   room.setName(newRoom.getName());
                   room.setFloor(newRoom.getFloor());
                   room.setDorm(newRoom.getDorm());
                   room.setSlots(newRoom.getSlots());
                   room.setBasePrice(newRoom.getBasePrice());
                   room.setStatus(newRoom.getStatus());

                    return roomRepository.save(room);
                }).orElseGet(() -> null);

        if(updateRoom != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateRoom)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
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
