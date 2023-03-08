package us.thedorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import us.thedorm.models.*;
import us.thedorm.repositories.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    BookingScheduleRepository bookingScheduleRepository;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private BookingRequestRepository bookingRequestRepository;
    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Billing addBilling(BookingRequest bookingRequest) {
        Billing newBilling = Billing
                .builder()
                .userInfo(UserInfo.builder().id(bookingRequest.getUserInfo().getId()).build())
                .type(Billing.Type.slot)
                .cost(bookingRequest.getSlot().getRoom().getBasePrice().getSlotPrice())
                .status(Billing.Status.Paid)
                .createdDate(new Date())
                .payDate(new Date())
                .build();
        return billingRepository.save(newBilling);
    }

    public ResidentHistory addResidentHistory(BookingRequest bookingRequest) {

        Long userId = bookingRequest.getUserInfo().getId();
        Optional<Billing> bill = billingRepository.findTopByUserInfo_IdAndTypeOrderByIdDesc(userId, Billing.Type.slot);
        if (bill.isPresent()) {
            bill.get().setStatus(Billing.Status.Paid);
            bill.get().setPayDate(new Date());
            billingRepository.save(bill.get());
            ResidentHistory newResidentHistory = ResidentHistory
                    .builder()
                    .userInfo(UserInfo.builder().id(userId).build())
                    .slot(Slot.builder().id(bookingRequest.getSlot().getId()).build())
                    .startDate(bookingRequest.getStartDate())
                    .endDate(bookingRequest.getEndDate())
                    .build();
            return residentHistoryRepository.save(newResidentHistory);
        }

        return null;
    }

    public void updateBookingRequest(BookingRequest newBookingRequest, BookingRequest booking_request) {
        Optional<UserInfo> user = userInfoRepository.findById(booking_request.getUserInfo().getId());
        Optional<ResidentHistory> residentHistory = residentHistoryRepository.findTopByUserInfo_IdOrderByIdDesc(user.get().getId());
        Optional<Slot> slot = slotRepository.findById(booking_request.getSlot().getId());
        Optional<Billing> billing = billingRepository.findTopByUserInfo_IdAndTypeOrderByIdDesc(booking_request.getUserInfo().getId(), Billing.Type.slot);

        if (newBookingRequest.getStatus().equals(BookingRequest.Status.Accept)) {
            addResidentHistory(booking_request);
            if (booking_request.getSlot().getId() == residentHistory.get().getSlot().getId()) {
                addResidentHistory(booking_request).setCheckinDate(residentHistory.get().getCheckinDate());
            }

        } else if ( newBookingRequest.getStatus().equals(BookingRequest.Status.Decline)) {
            user.get().setBalance(user.get().getBalance() + booking_request.getSlot().getRoom().getBasePrice().getSlotPrice());
            slot.get().setStatus(Slot.Status.Available);
            slotRepository.save(slot.get());
            billing.get().setStatus(Billing.Status.Refund);
            billing.get().setRefundDate(new Date());
            billingRepository.save(billing.get());

        }

        booking_request.setStatus(newBookingRequest.getStatus());
    }


   public ResponseEntity<ResponseObject> checkBooking(BookingRequest newBookingRequest,UserInfo user) {


       Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(newBookingRequest.getSlot().getRoom().getDorm().getBranch().getId());
       newBookingRequest.setUserInfo(user);
       newBookingRequest.setStartDate(bookingSchedule.get().getStartDate());
       newBookingRequest.setEndDate(bookingSchedule.get().getEndDate());
       newBookingRequest.setCreatedDate(new Date());
       newBookingRequest.setStatus(BookingRequest.Status.Processing);

       Optional<BookingRequest> foundBookingRequest = bookingRequestRepository.findTopByUserInfo_IdAndStatusIsNotOrderByIdDesc(user.getId(), BookingRequest.Status.Decline);


       if (foundBookingRequest.get().getStartDate().equals(bookingSchedule.get().getStartDate()) && foundBookingRequest.get().getEndDate().equals(bookingSchedule.get().getEndDate())) {
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("", "You had book in this semester", "")
           );
       }

       Optional<Slot> bookslot = slotRepository.findById(newBookingRequest.getSlot().getId());


       if (bookslot.isPresent()) {
           if (bookslot.get().getStatus() == Slot.Status.Available) {
               bookslot.get().setStatus(Slot.Status.NotAvailable);
               slotRepository.save(bookslot.get());
               int cost = bookslot.get().getRoom().getBasePrice().getSlotPrice();
               user.setBalance(user.getBalance() - cost);
               userInfoRepository.save(user);
               addBilling(newBookingRequest);

           } else {
               return ResponseEntity.status(HttpStatus.OK).body(
                       new ResponseObject("OK", "Slot is not available", "")
               );
           }
       }


       return ResponseEntity.status(HttpStatus.OK).body(
               new ResponseObject("OK", "Insert successfully", bookingRequestRepository.save(newBookingRequest))
       );
   }







// public boolean checkIsBook(UserInfo userInfo,Optional<BookingRequest> bookingRequest,Optional<BookingSchedule> bookingSchedule) {
//
//     if (bookingRequest.get().getStartDate().equals(bookingSchedule.get().getStartDate())) {
//         bookingSchedule.get();
//         bookingSchedule.get();
//         return true;
//     }
//   return  false;
// }


// public boolean checkReset(){
//  List<Slot> slots = slotRepository.findAll();
//
//       for (Slot slot : slots) {
//          if(slot.getStatus().equals(Slot.Status.NotAvailable)){
//           return false;
//          }
//   }
//  return true;
// }




// public ResidentHistory updateBalance(BookingRequest bookingRequest){
//  Long userId = bookingRequest.getUserInfo().getId();
//  Optional<Billing> bill = billingRepository.findTopByUserInfo_IdAndTypeOrderByIdDesc(userId,Billing.Type.slot);
//  if(bill.isPresent()){
//   bill.get().setStatus(Billing.Status.Refund);
//   bill.get().setRefundDate(new Date());
//   billingRepository.save(bill.get());
//   ResidentHistory newResidentHistory = ResidentHistory
//           .builder()
//           .userInfo(UserInfo.builder().id(userId).balance())
//  }
 }


// public BookingRequest checkBookingRequest(UserInfo userInfo){
//         BookingRequest bookingRequest = (BookingRequest) bookingRequestRepository.getBookingRequestByUserInfoId(userInfo.getId());
//
//  if(bookingRequest!= null){
//   bookingRequest.setStatus(StatusBookingRequest.Decline);
//  }
//
//  return bookingRequestRepository.save(bookingRequest);
//
// }



