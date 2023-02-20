package us.thedorm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Billing;
import us.thedorm.models.BookingRequest;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Room;
import us.thedorm.repositories.BillingRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/billings")
public class BillingController {
    @Autowired
    public BillingRepository billingRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllBilling() {
        List<Billing> foundBilling =billingRepository.findAll();
        if(foundBilling.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("empty", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", foundBilling)
        );

    }


    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Billing> found = billingRepository.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody Billing newBilling) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", billingRepository.save(newBilling))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody Billing newBilling, @PathVariable Long id) {
      Billing update = billingRepository.findById(id)
                .map(billing -> {
                    billing.setType(newBilling.getType());
                    billing.setCost(newBilling.getCost());
                    billing.setStatus(newBilling.getStatus());
                    billing.setCreatedDate(newBilling.getCreatedDate());
                    billing.setDeadlineDate(newBilling.getDeadlineDate());
                    billing.setPayDate(newBilling.getPayDate());
                    billing.setUserInfo(newBilling.getUserInfo());

                    return billingRepository.save(billing);
                }).orElseGet(() -> null);

        if(update != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> delete(@PathVariable Long id) {
        boolean exists = billingRepository.existsById(id);
        if (exists) {
            billingRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", "")
        );
    }

}
