package us.thedorm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.repositories.SlotRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/slots")
public class SlotController {
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllslots() {
        List<Slot> foundSlots = slotRepository.findAll();
        if (foundSlots.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("empty", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundSlots)
        );

    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Slot> foundslot = slotRepository.findById(id);
        return foundslot.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundslot)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @GetMapping("/room/{id}")
    ResponseEntity<ResponseObject> findByRoomId(@PathVariable Long id) {
        List<Slot> foundSlots = slotRepository.getSlotsByRoom_Id(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundSlots));
    }
    @GetMapping("/room/{id}/available")
    ResponseEntity<ResponseObject> findslotsByRoomId(@PathVariable Long id) {
        List<Slot> foundSlots = slotRepository.getSlotsByRoom_IdAndStatus(id, Slot.Status.Available);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundSlots));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insertslot(@RequestBody Slot newSlot) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", slotRepository.save(newSlot))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateslot(@RequestBody Slot newSlot, @PathVariable Long id) {
        Slot updateSlot = slotRepository.findById(id)
                .map(slot -> {
                    slot.setName(newSlot.getName());
                    slot.setRoom(newSlot.getRoom());
                    slot.setStatus(newSlot.getStatus());
                    slot.setFacilities(newSlot.getFacilities());
                    slot.setResidentHistories(newSlot.getResidentHistories());
                    return slotRepository.save(slot);

                }).orElseGet(() -> null);
        if(updateSlot != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateSlot)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteslot(@PathVariable Long id) {
        boolean exists = slotRepository.existsById(id);
        if (exists) {
            slotRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @GetMapping("/get-old-slot")
    ResponseEntity<ResponseObject> getOldSlot() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());

        return residentHistory.map(history -> ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("", "OK", history.getSlot())
        )).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Ok", "false")
        ));

    }
}
