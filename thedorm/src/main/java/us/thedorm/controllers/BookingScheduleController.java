package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.BookingScheduleRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/booking-schedule")
public class BookingScheduleController {

    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;

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



}
