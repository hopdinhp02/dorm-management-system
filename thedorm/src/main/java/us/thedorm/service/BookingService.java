package us.thedorm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thedorm.models.*;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.BookingRequestRepository;
import us.thedorm.repositories.ResidentHistoryRepository;

import java.util.Date;
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
 private SlotRepository slotRepository;

 public Billing addBilling(BookingRequest bookingRequest){
  Billing newBilling = Billing
          .builder()
          .userInfo(UserInfo.builder().id(bookingRequest.getUserInfo().getId()).build())
          .type(Billing.Type.slot)
          .cost(bookingRequest.getSlot().getRoom().getBasePrice().getSlotPrice())
          .status(Billing.Status.Unpaid)
          .createdDate(new Date())
          .build();
  return billingRepository.save(newBilling);
 }

 public ResidentHistory addResidentHistory(BookingRequest bookingRequest){

  Long userId = bookingRequest.getUserInfo().getId();
  Optional<Billing> bill = billingRepository.findTopByUserInfo_IdAndTypeOrderByIdDesc(userId,Billing.Type.slot);
  if(bill.isPresent()){
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



