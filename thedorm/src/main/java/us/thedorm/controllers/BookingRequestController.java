package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.repositories.BookingRequestRepository;
import us.thedorm.repositories.HistoryBookingRequestRepository;
import us.thedorm.service.BookingService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SlotRepository slotRepository;
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
    ResponseEntity<?> insertBookingRequest(@RequestBody BookingRequest newBookingRequest){

        UserInfo user =  (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate firstDateOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        Date startDate = Date.from(firstDateOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Instant lastDateOfNextMonth = firstDateOfNextMonth.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);
        Date endDate = Date.from(lastDateOfNextMonth);
        newBookingRequest.setStartDate(startDate);
        newBookingRequest.setEndDate(endDate);
        newBookingRequest.setCreatedDate(new Date());
        newBookingRequest.setStatus(BookingRequest.Status.Processing);
        Optional<Slot> bookslot = slotRepository.findById(newBookingRequest.getSlot().getId()) ;

        if(bookslot.isPresent()){
            if(bookslot.get().getStatus()== Slot.Status.Available){
                bookslot.get().setStatus(Slot.Status.NotAvailable);
                slotRepository.save(bookslot.get());
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "This slot is not available", ""));
            }

        }
        newBookingRequest.setUserInfo(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", bookingRequestRepository.save(newBookingRequest))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateBookingRequest(@RequestBody BookingRequest newBookingRequest, @PathVariable Long id){
        BookingRequest updateBookingRequest = bookingRequestRepository.findById(id)
                .map(booking_request -> {

                    if(booking_request.getStatus().equals(BookingRequest.Status.Processing) && newBookingRequest.getStatus().equals(BookingRequest.Status.Paying) ){
                        bookingService.addBilling(booking_request);
                    }else if(booking_request.getStatus().equals(BookingRequest.Status.Paying) && newBookingRequest.getStatus().equals(BookingRequest.Status.Accept) ){
                        bookingService.addResidentHistory(booking_request);
                    }
                else if(booking_request.getStatus().equals(BookingRequest.Status.Accept) && newBookingRequest.getStatus().equals(BookingRequest.Status.Decline) ){
            bookingService.addResidentHistory(booking_request);
        }

                    booking_request.setStatus(newBookingRequest.getStatus());

                    return bookingRequestRepository.save(booking_request);
                }).orElseGet(()-> null);
        if(updateBookingRequest != null){
//            historyBookingRequestRepository.save(recordChangeInBooking(updateBookingRequest));
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
    @GetMapping("/userInfo/is-booked")
    ResponseEntity<ResponseObject> checkUserIdInBookingRequest() {
        UserInfo user =  (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BookingRequest> foundBookingRequest = bookingRequestRepository.findTopByUserInfo_IdAndStatusIsNotOrderByIdDesc(user.getId(), BookingRequest.Status.Decline);

        if(foundBookingRequest.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "Booked", true)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Book", false)
        );
    }

    private HistoryBookingRequest recordChangeInBooking(BookingRequest booking){
        HistoryBookingRequest historyBookingRequest = new HistoryBookingRequest();
        historyBookingRequest.setBookingRequest(booking);
//        historyBookingRequest.setStatus(booking.getStatus());
        historyBookingRequest.setNote(booking.getNote());
        historyBookingRequest.setCreatedDate(booking.getCreatedDate());
        historyBookingRequest.setUserInfo(booking.getUserInfo());
        return historyBookingRequest;
    }

}
