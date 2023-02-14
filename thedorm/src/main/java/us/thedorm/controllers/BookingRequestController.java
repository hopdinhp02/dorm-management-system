package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.booking_request;
import us.thedorm.models.dorm;
import us.thedorm.repositories.BookingRequestRepository;
import us.thedorm.repositories.DormRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/booking_Requests")
public class BookingRequestController {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllbBookingRequests() {
        List<booking_request> foundBookingRequests = bookingRequestRepository.findAll();
        if(foundBookingRequests.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBookingRequests)
        );

    }
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<booking_request> foundBookingRequest = bookingRequestRepository.findById(id);
        return foundBookingRequest.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBookingRequest)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("/")
    ResponseEntity<ResponseObject> insertBookingRequest(@RequestBody booking_request newBookingRequest){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bookingRequestRepository.save(newBookingRequest))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateBookingRequest(@RequestBody booking_request newBookingRequest, @PathVariable Long id){
        booking_request updateBookingRequest = bookingRequestRepository.findById(id)
                .map(booking_request -> {

                    booking_request.setUser_info(newBookingRequest.getUser_info());
                    booking_request.setDorms(newBookingRequest.getDorms());
                    booking_request.setFloor(newBookingRequest.getFloor());
                    booking_request.setNote(newBookingRequest.getNote());
                    booking_request.setPrice(newBookingRequest.getPrice());
                    booking_request.setStart_date(newBookingRequest.getStart_date());
                    booking_request.setEnd_date(newBookingRequest.getEnd_date());
                    booking_request.setCreated_date(newBookingRequest.getCreated_date());
                    booking_request.setStatus(newBookingRequest.getStatus());


                    return bookingRequestRepository.save(booking_request);

                }).orElseGet(()-> bookingRequestRepository.save(newBookingRequest));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert Product successfully", updateBookingRequest)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteBookingRequest(@PathVariable Long id){
        boolean exists = bookingRequestRepository.existsById(id);
        if(exists){
            bookingRequestRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

}
