package us.thedorm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;

import us.thedorm.repositories.*;
import us.thedorm.service.BookingService;
import us.thedorm.service.RoomStatusService;


import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/slots")
public class SlotController {
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private RoomStatusService roomStatusService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private DormRepository dormRepository;
    @Autowired
    private BranchRepository branchRepository;
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

    @GetMapping("/slot-available/room/{id}")
    ResponseEntity<ResponseObject> findslotsAvailable(@PathVariable Long id) {

        List<Slot> foundSlots = bookingService.findAllSlotAvailable(id);

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

    @GetMapping("/available")
    ResponseEntity<ResponseObject> getNumOfAvailableSlotInYear(@RequestParam String year,
                                                                   @RequestParam(required = false) String roomid,
                                                                   @RequestParam(required = false) String dormid,
                                                                   @RequestParam(required = false) String branchid)
    {
        try{
            int yearInt = Integer.parseInt(year);
            int[] numOfAvailable = null;
            if(roomid != null){
            long roomId = Long.parseLong(roomid);
            Optional<Room> room = roomRepository.findById(roomId);
            if(room.isPresent()){
                numOfAvailable = roomStatusService.getNumOfAvailableSlotByMonthInDayOfRoom(room.get(), yearInt);
            }
            }else if(dormid != null){
                long dormId = Long.parseLong(dormid);
                Optional<Dorm> dorm = dormRepository.findById(dormId);
                if(dorm.isPresent()){
                    numOfAvailable = roomStatusService.getNumOfAvailableSlotByMonthInDayOfDorm(dorm.get(), yearInt);
                }
            }else if(branchid != null){
                long branchId = Long.parseLong(branchid);
                Optional<Branch> branch = branchRepository.findById(branchId);
                if(branch.isPresent()){
                    numOfAvailable = roomStatusService.getNumOfAvailableSlotByMonthInDayOfBranch(branch.get(), yearInt);
                }
            }else{
                numOfAvailable = roomStatusService.getNumOfAvailableSlotByMonthInDay(yearInt);
            }
            if(numOfAvailable.length > 0){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", numOfAvailable)
                );
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }

    @GetMapping("/booked")
    ResponseEntity<ResponseObject> getNumOfBookedSlotInYear(@RequestParam String year,
                                                               @RequestParam(required = false) String roomid,
                                                               @RequestParam(required = false) String dormid,
                                                               @RequestParam(required = false) String branchid)
    {
        try{
            int yearInt = Integer.parseInt(year);
            int[] numOfBooked = null;
            if(roomid != null){
                long roomId = Long.parseLong(roomid);
                Optional<Room> room = roomRepository.findById(roomId);
                if(room.isPresent()){
                    numOfBooked = roomStatusService.getNumOfBookedSlotByMonthInDayOfRoom(room.get(), yearInt);
                }
            }else if(dormid != null){
                long dormId = Long.parseLong(dormid);
                Optional<Dorm> dorm = dormRepository.findById(dormId);
                if(dorm.isPresent()){
                    numOfBooked = roomStatusService.getNumOfBookedSlotByMonthInDayOfDorm(dorm.get(), yearInt);
                }
            }else if(branchid != null){
                long branchId = Long.parseLong(branchid);
                Optional<Branch> branch = branchRepository.findById(branchId);
                if(branch.isPresent()){
                    numOfBooked = roomStatusService.getNumOfBookedSlotByMonthInDayOfBranch(branch.get(), yearInt);
                }
            }else{
                numOfBooked = roomStatusService.getNumOfBookedSlotByMonthInDay(yearInt);
            }
            if(numOfBooked.length > 0){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", numOfBooked)
                );
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }

}
