package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.BookingRequest;
import us.thedorm.models.HistoryBookingRequest;
import us.thedorm.repositories.BookingRequestRepository;
import us.thedorm.repositories.HistoryBookingRequestRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/booking-requests")
public class BookingRequestController {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;
    @Autowired
    private HistoryBookingRequestRepository historyBookingRequestRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllBookingRequests() {
        List<BookingRequest> foundBookingRequests = bookingRequestRepository.findAll();
        if(foundBookingRequests.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("empty", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBookingRequests)
        );

    }
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<BookingRequest> foundBookingRequest = bookingRequestRepository.findById(id);
        return foundBookingRequest.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", foundBookingRequest)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insertBookingRequest(@RequestBody BookingRequest newBookingRequest){
//        BookingRequest booking = bookingRequestRepository.save(newBookingRequest);
//        System.out.println(booking);
//        historyBookingRequestRepository.save(recordChangeInBooking(booking));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bookingRequestRepository.save(newBookingRequest))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateBookingRequest(@RequestBody BookingRequest newBookingRequest, @PathVariable Long id){
        BookingRequest updateBookingRequest = bookingRequestRepository.findById(id)
                .map(booking_request -> {
//                    booking_request.setUserInfo(newBookingRequest.getUserInfo());
//                    booking_request.setBed(newBookingRequest.getBed());
//                    booking_request.setNote(newBookingRequest.getNote());
//                    booking_request.setStartDate(newBookingRequest.getStartDate());
//                    booking_request.setEndDate(newBookingRequest.getEndDate());
//                    booking_request.setCreatedDate(newBookingRequest.getCreatedDate());
                    booking_request.setStatus(newBookingRequest.getStatus());
                    return bookingRequestRepository.save(booking_request);
                }).orElseGet(()-> null);
        if(updateBookingRequest != null){
            historyBookingRequestRepository.save(recordChangeInBooking(updateBookingRequest));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", updateBookingRequest)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
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

    private HistoryBookingRequest recordChangeInBooking(BookingRequest booking){
        HistoryBookingRequest historyBookingRequest = new HistoryBookingRequest();
        historyBookingRequest.setBookingRequest(booking);
        historyBookingRequest.setStatus(booking.getStatus());
        historyBookingRequest.setNote(booking.getNote());
        historyBookingRequest.setCreatedDate(booking.getCreatedDate());
        historyBookingRequest.setUserInfo(booking.getUserInfo());
        return historyBookingRequest;
    }

}
