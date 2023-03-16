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
    private NotificationService notificationService;

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
    private NotificationRepository notificationRepository;

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

        if (booking_request.getStatus().equals(BookingRequest.Status.Processing) && newBookingRequest.getStatus().equals(BookingRequest.Status.Accept)) {
            String titleBooking ="Booking Sucessfull";
            String contentBooking ="Congratulation on your sucessfull booking: Room: "
                    +booking_request.getSlot().getRoom().getName()+
                    ", Slot: "+booking_request.getSlot().getName()+" on "+ booking_request.getCreatedDate();
            Long userid = booking_request.getUserInfo().getId();
            notificationService.sendNotification(titleBooking,contentBooking,userid);
//            Notification newNotification = Notification.builder()
//                    .title(titleBooking)
//                    .content(contentBooking)
//                    .createdDate(new Date())
//                    .userInfo(UserInfo.builder().id(booking_request.getUserInfo().getId()).build())
//                    .build();
//            notificationRepository.save(newNotification);

            if (booking_request.getSlot().getId() == residentHistory.get().getSlot().getId()) {
                addResidentHistory(booking_request).setCheckinDate(residentHistory.get().getCheckinDate());
            } else {
                addResidentHistory(booking_request);
            }

        } else if (booking_request.getStatus().equals(BookingRequest.Status.Processing) && newBookingRequest.getStatus().equals(BookingRequest.Status.Decline)) {
            user.get().setBalance(user.get().getBalance() + booking_request.getSlot().getRoom().getBasePrice().getSlotPrice());
            slot.get().setStatus(Slot.Status.Available);
            slotRepository.save(slot.get());
            billing.get().setStatus(Billing.Status.Refund);
            billing.get().setRefundDate(new Date());
            billingRepository.save(billing.get());

        }

        booking_request.setStatus(newBookingRequest.getStatus());
    }


    public ResponseEntity<ResponseObject> checkBooking(BookingRequest newBookingRequest, UserInfo user) {
        Optional<Slot> bookslot = slotRepository.findById(newBookingRequest.getSlot().getId());
        if (bookslot.isPresent()) {
            newBookingRequest.setSlot(bookslot.get());
            Optional<BookingSchedule> bookingSchedule = bookingScheduleRepository.findBookingScheduleByBranch_Id(newBookingRequest.getSlot().getRoom().getDorm().getBranch().getId());

            newBookingRequest.setUserInfo(user);
            newBookingRequest.setStartDate(bookingSchedule.get().getStartDate());
            newBookingRequest.setEndDate(bookingSchedule.get().getEndDate());
            newBookingRequest.setCreatedDate(new Date());
            newBookingRequest.setStatus(BookingRequest.Status.Processing);

            Optional<BookingRequest> foundBookingRequest = bookingRequestRepository.findTopByUserInfo_IdAndStatusIsNotOrderByIdDesc(user.getId(), BookingRequest.Status.Decline);


            if (foundBookingRequest.isPresent() && foundBookingRequest.get().getStartDate().equals(bookingSchedule.get().getStartDate()) && foundBookingRequest.get().getEndDate().equals(bookingSchedule.get().getEndDate())) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", "You had book in this semester", "")
                );
            }


            if (bookslot.get().getStatus() == Slot.Status.Available) {
                bookslot.get().setStatus(Slot.Status.NotAvailable);
                slotRepository.save(bookslot.get());
                int cost = bookslot.get().getRoom().getBasePrice().getSlotPrice();
                user.setBalance(user.getBalance() - cost);
                userInfoRepository.save(user);
                addBilling(newBookingRequest);
                //
                String titleBooking ="Booking Processing";
                String contentBooking ="Wait a second. You have requested to book : Room: "
                        +newBookingRequest.getSlot().getRoom().getName()+
                        ", Slot: "+newBookingRequest.getSlot().getName()+" on "+ newBookingRequest.getCreatedDate();
                Long userid = user.getId();
                notificationService.sendNotification(titleBooking,contentBooking,userid);
//                Notification newNotification = Notification.builder()
//                        .title(titleBooking)
//                        .content(contentBooking)
//                        .createdDate(new Date())
//                        .userInfo(UserInfo.builder().id(user.getId()).build())
//                        .build();
//                notificationRepository.save(newNotification);
//
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

}


