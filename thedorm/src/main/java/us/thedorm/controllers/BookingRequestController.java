package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.*;
import us.thedorm.service.BookingService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/booking-requests")
public class BookingRequestController {
    @Autowired
    private BookingRequestRepository bookingRequestRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private HistoryBookingRequestRepository historyBookingRequestRepository;

    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private BookingScheduleRepository bookingScheduleRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllBookingRequests() {
        List<BookingRequest> foundBookingRequests = bookingRequestRepository.findAll();
        if (foundBookingRequests.size() == 0) {
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
    ResponseEntity<?> insertBookingRequest(@RequestBody BookingRequest newBookingRequest) {

        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate firstDateOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        Date startDate = Date.from(firstDateOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Instant lastDateOfNextMonth = firstDateOfNextMonth.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);
        Date endDate = Date.from(lastDateOfNextMonth);
        newBookingRequest.setStartDate(startDate);
        newBookingRequest.setEndDate(endDate);
        newBookingRequest.setCreatedDate(new Date());
        newBookingRequest.setStatus(BookingRequest.Status.Processing);


        Optional<Slot> bookslot = slotRepository.findById(newBookingRequest.getSlot().getId());


        if (bookslot.isPresent()) {
            if (bookslot.get().getStatus() == Slot.Status.Available) {
                bookslot.get().setStatus(Slot.Status.NotAvailable);
                slotRepository.save(bookslot.get());
                int cost = bookslot.get().getRoom().getBasePrice().getSlotPrice();
                user.setBalance(user.getBalance() - cost);
                userInfoRepository.save(user);
            } else {
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
    ResponseEntity<ResponseObject> updateBookingRequest(@RequestBody BookingRequest newBookingRequest, @PathVariable Long id) {

        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Optional<BookingRequest> bookingRequest = bookingRequestRepository.findById(id);
        BookingRequest updateBookingRequest = bookingRequestRepository.findById(id)
                .map(booking_request -> {

                    if (booking_request.getStatus().equals(BookingRequest.Status.Processing) && newBookingRequest.getStatus().equals(BookingRequest.Status.Paying)) {
                        bookingService.addBilling(booking_request);
                    } else if (booking_request.getStatus().equals(BookingRequest.Status.Paying) && newBookingRequest.getStatus().equals(BookingRequest.Status.Accept)) {
                        bookingService.addResidentHistory(booking_request);
                    } else if (newBookingRequest.getStatus().equals(BookingRequest.Status.Decline)) {

                        user.setBalance(user.getBalance() + booking_request.getSlot().getRoom().getBasePrice().getSlotPrice());
                    }


                    booking_request.setStatus(newBookingRequest.getStatus());

                    return bookingRequestRepository.save(booking_request);
                }).orElseGet(() -> null);

        if (updateBookingRequest != null) {
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
    ResponseEntity<ResponseObject> deleteBookingRequest(@PathVariable Long id) {
        boolean exists = bookingRequestRepository.existsById(id);
        if (exists) {
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
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BookingRequest> foundBookingRequest = bookingRequestRepository.findTopByUserInfo_IdAndStatusIsNotOrderByIdDesc(user.getId(), BookingRequest.Status.Decline);

        if (foundBookingRequest.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "Booked", true)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Book", false)
        );
    }

    private HistoryBookingRequest recordChangeInBooking(BookingRequest booking) {
        HistoryBookingRequest historyBookingRequest = new HistoryBookingRequest();
        historyBookingRequest.setBookingRequest(booking);
//        historyBookingRequest.setStatus(booking.getStatus());
        historyBookingRequest.setNote(booking.getNote());
        historyBookingRequest.setCreatedDate(booking.getCreatedDate());
        historyBookingRequest.setUserInfo(booking.getUserInfo());
        return historyBookingRequest;
    }

    @GetMapping("/guard/check-in")
    ResponseEntity<ResponseObject> checkIn(@RequestBody UserInfo resident) {

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(resident.getId());
        if (residentHistory.isPresent()) {
            residentHistory.get().setCheckinDate(new Date());
            residentHistoryRepository.save(residentHistory.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "checked", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", "")
        );
    }

    @GetMapping("/guard/check-out")
    ResponseEntity<ResponseObject> checkOut(@RequestBody UserInfo resident) {

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(resident.getId());
        if (residentHistory.isPresent()) {
            residentHistory.get().setCheckoutDate(new Date());
            residentHistoryRepository.save(residentHistory.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("", "checked", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", "")
        );
    }


//    @GetMapping("/keep-booking")
//    ResponseEntity<ResponseObject> KeepBooking(@RequestBody BookingRequest newBookingRequest) {
//
//        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());
//
//
//        if (residentHistory.isPresent()) {
//            Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.
//                    findBookingScheduleByBranch_Id(residentHistory.get().getSlot().getRoom().getDorm().getBranch().getId());
//
//            if (bookingSchedule.isPresent()) {
//                if (bookingSchedule.get().getKeepStartDate().before(residentHistory.get().getEndDate())) {
//                    insertBookingRequest(newBookingRequest);
//
//                }
//
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("", "Ok", ""));
//
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("", "Not Ok", "")
//        );
//
//    }


    @GetMapping("/check-day-booking")
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


    @GetMapping("/check-day-keep")
    ResponseEntity<ResponseObject> checkDayKeep(@RequestBody Branch branch) {

        Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(branch.getId());
        Date date = new Date();
        if (bookingSchedule.isPresent()) {
            if (date.after(bookingSchedule.get().getKeepStartDate()) && date.before(bookingSchedule.get().getKeepEndDate())) {

//                List<Slot> slots = slotRepository.findAll();
//
//                for (Slot slot : slots) {
//                    slot.setStatus(Slot.Status.Available);
//                    slotRepository.save(slot);
//                }
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "OK", true)
                );
            }

        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Valid", false)
        );

    }

    @GetMapping("/check-living")
    ResponseEntity<ResponseObject> checkLiving() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());

//        Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.
//                findBookingScheduleByBranch_Id(residentHistory.get().getSlot().getRoom().getDorm().getBranch().getId());
//

        Date date = new Date();
        if (residentHistory.isPresent()) {
            if (residentHistory.get().getEndDate().after(date)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "OK", true)
                );

            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("", "OK", false)
        );
    }


    @GetMapping("/get-old-slot")
    ResponseEntity<ResponseObject> getOldSlot() {
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.getId());

//       residentHistory.get().getSlot().setStatus(Slot.Status.Available);

        return residentHistory.map(history -> ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("", "OK", history.getSlot())
        )).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Not Ok", "false")
        ));

    }

    @GetMapping("/reset-slots")
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

            }
        };

            timer.schedule(task, bookingSchedule.get().getKeepStartDate());
            bookingSchedule.get().setReset(true);

           bookingScheduleRepository.save(bookingSchedule.get());

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




