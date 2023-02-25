package us.thedorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import us.thedorm.models.*;
import us.thedorm.repositories.BedRepository;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.BookingRequestRepository;
import us.thedorm.repositories.ResidentHistoryRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
 @Autowired
 private BillingRepository billingRepository;


 @Autowired
 private ResidentHistoryRepository residentHistoryRepository;
 @Autowired
 private BookingRequestRepository bookingRequestRepository;
 @Autowired
 private BedRepository bedRepository;

 public Billing addBilling(BookingRequest bookingRequest){
  Billing newBilling = Billing
          .builder()
          .userInfo(UserInfo.builder().id(bookingRequest.getUserInfo().getId()).build())
          .type(Billing.Type.Bed)
          .cost(bookingRequest.getBed().getRoom().getBasePrice().getBedPrice())
          .status(Billing.Status.Unpaid)
          .createdDate(new Date())
          .build();
  return billingRepository.save(newBilling);
 }




 public ResidentHistory addResidentHistory(BookingRequest bookingRequest){

  Long userId = bookingRequest.getUserInfo().getId();
  Optional<Billing> bill = billingRepository.findTopByUserInfo_IdAndTypeOrderByIdDesc(userId,Billing.Type.Bed);
  if(bill.isPresent()){
   bill.get().setStatus(Billing.Status.Paid);
   bill.get().setPayDate(new Date());
   billingRepository.save(bill.get());
   ResidentHistory newResidentHistory = ResidentHistory
           .builder()
           .userInfo(UserInfo.builder().id(userId).build())
           .bed(Bed.builder().id(bookingRequest.getBed().getId()).build())
           .startDate(bookingRequest.getStartDate())
           .endDate(bookingRequest.getEndDate())
           .build();
   return residentHistoryRepository.save(newResidentHistory);
  }

return null;
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


}
