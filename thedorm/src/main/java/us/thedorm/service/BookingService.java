package us.thedorm.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import us.thedorm.models.*;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.ResidentHistoryRepository;

import java.util.Date;


public class BookingService {
 private BillingRepository billingRepository;
 private ResidentHistoryRepository residentHistoryRepository;

 ResponseEntity<ResponseObject> insertBilling(@RequestBody Billing newBilling) {
  return ResponseEntity.status(HttpStatus.OK).body(
          new ResponseObject("OK", "Insert successfully", billingRepository.save(newBilling))
  );
 }
 ResponseEntity<ResponseObject> insertResidentHistory(@RequestBody ResidentHistory newResidentHistory) {
  return ResponseEntity.status(HttpStatus.OK).body(
          new ResponseObject("OK", "Insert successfully", residentHistoryRepository.save(newResidentHistory))
  );
 }

}
