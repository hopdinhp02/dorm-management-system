package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.BookingScheduleRepository;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.repositories.SlotRepository;


import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/booking-schedule")
public class BookingScheduleController {
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;
    @Autowired
    private SlotRepository slotRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAll() {
        List<BookingSchedule> founds = bookingScheduleRepository.findAll();
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
        Optional<BookingSchedule> founds = bookingScheduleRepository.findById(id);
        return founds.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", founds)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")

    ResponseEntity<ResponseObject> insertBookingSchedule(@RequestBody BookingSchedule newBookingSchedule) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bookingScheduleRepository.save(newBookingSchedule))
        );
    }


    @PutMapping("/{id}")

    ResponseEntity<ResponseObject> update(@RequestBody BookingSchedule newBookingSchedule, @PathVariable Long id) {

        BookingSchedule bookingSchedule = bookingScheduleRepository.findById(id)
                .map(booking_Schedule -> {
                    booking_Schedule.setBranch(newBookingSchedule.getBranch());
                    booking_Schedule.setKeepStartDate(newBookingSchedule.getKeepStartDate());
                    booking_Schedule.setKeepEndDate(newBookingSchedule.getKeepEndDate());
                    booking_Schedule.setNewStartDate(newBookingSchedule.getNewStartDate());
                    booking_Schedule.setNewEndDate(newBookingSchedule.getNewEndDate());
                    booking_Schedule.setStartDate(newBookingSchedule.getStartDate());
                    booking_Schedule.setEndDate(newBookingSchedule.getEndDate());

                    return bookingScheduleRepository.save(booking_Schedule);
                }).orElseGet(() -> null);
        if(bookingSchedule != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert  successfully", bookingSchedule)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );

    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {

        boolean exists = bookingScheduleRepository.existsById(id);
        if (exists) {
            bookingScheduleRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @PostMapping("/check-day-booking")
    ResponseEntity<ResponseObject> checkDayBooking(@RequestBody Branch branch) {
        Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(branch.getId());

        Date date = new Date();
        if (bookingSchedule.isPresent()) {
            if (date.after(bookingSchedule.get().getNewStartDate()) && date
                    .before(bookingSchedule.get().getNewEndDate())) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "OK", true)
                );
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Valid", false)
        );
    }


    @PostMapping("/check-day-keep")
    ResponseEntity<ResponseObject> checkDayKeep(@RequestBody Branch branch) {

        Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(branch.getId());
        Date date = new Date();
        if (bookingSchedule.isPresent()) {
            if (date.after(bookingSchedule.get().getKeepStartDate()) && date.before(bookingSchedule.get().getKeepEndDate())) {

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "OK", true)
                );
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Valid", false)
        );

    }



    @PostMapping("/reset-slots")
    ResponseEntity<ResponseObject> resetSlot(@RequestBody Branch branch) {

        Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(branch.getId());

        if(checkReset(bookingSchedule)) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Cant Reset", "")
            );
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                List<Slot> slots = slotRepository.findAll();
                for (Slot slot : slots) {
                    slot.setStatus(Slot.Status.Available);
                    slotRepository.save(slot);
                }
                bookingSchedule.get().setReset(true);
                bookingScheduleRepository.save(bookingSchedule.get());
            }
        };
//                bookingSchedule.get().setReset(true);
//                bookingScheduleRepository.save(bookingSchedule.get());
        timer.schedule(task, bookingSchedule.get().getKeepStartDate());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", "")
        );

    }

    public boolean checkReset(Optional<BookingSchedule> bookingSchedule){
        if(bookingSchedule.get().isReset()){
            return true;
        }
        return false;
    }



}
