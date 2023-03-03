package us.thedorm.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.Facility;
import us.thedorm.models.ResponseObject;
import us.thedorm.models.Slot;
import us.thedorm.repositories.SlotRepository;
import us.thedorm.service.ProfitService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/profit")
public class ProfitController {
    @Autowired
    private ProfitService profitService;
    @Autowired
    private SlotRepository slotRepository;
    @GetMapping("/slots/{id}/revenue")
    ResponseEntity<ResponseObject> getSlotRevenueByDay(@RequestBody JsonNode requestBody, @PathVariable Long id) {
        String dateJson = requestBody.get("date").asText();
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = dateFormat.parse(dateJson);
            Optional<Slot> slot = slotRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", profitService.getSlotRevenueByDay(slot.get(), date))
            );
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }



    }

    @GetMapping("/slots/{id}/maintenance")
    ResponseEntity<ResponseObject> getSlotMaintenanceFeeByDay(@RequestBody JsonNode requestBody, @PathVariable Long id) throws ParseException {

        String dateJson = requestBody.get("date").asText();
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = dateFormat.parse(dateJson);
            Optional<Slot> slot = slotRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", profitService.getSlotMaintenanceFeeByDay(slot.get(), date))
            );
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }

    @GetMapping("/slots/{id}/depreciation")
    ResponseEntity<ResponseObject> getSlotDepreciationByDay(@RequestBody JsonNode requestBody, @PathVariable Long id) {
        String dateJson = requestBody.get("date").asText();
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = dateFormat.parse(dateJson);
            Optional<Slot> slot = slotRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", profitService.getSlotDepreciationByDay(slot.get(), date))
            );
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }


    }





    @GetMapping("/slots/{id}/{type}/month")
    ResponseEntity<ResponseObject> getSlotDepreciationByMonth(@RequestBody JsonNode requestBody, @PathVariable Long id, @PathVariable String type) throws ParseException {
        String month = requestBody.get("month").asText();
        String year = requestBody.get("year").asText();
        try{
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            Optional<Slot> slot = slotRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", profitService.getSlotMoneyByMonth(type,slot.get(), monthInt, yearInt))
            );
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }
    }


    @GetMapping("/slots/{id}/{type}/year")
    ResponseEntity<ResponseObject> getSlotMoneyByYear(@RequestBody JsonNode requestBody, @PathVariable Long id, @PathVariable String type) {
        String year = requestBody.get("year").asText();
        try{

            int yearInt = Integer.parseInt(year);
            Optional<Slot> slot = slotRepository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "", profitService.getSlotMoneyByYear(type,slot.get(), yearInt))
            );
        }catch (NumberFormatException | ParseException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }
    }

    @GetMapping("/slots/{id}/{type}/days-in-month")
    ResponseEntity<ResponseObject> getSlotRevenueByDaysInMonth(@RequestBody JsonNode requestBody, @PathVariable Long id, @PathVariable String type) {
        String month = requestBody.get("month").asText();
        String year = requestBody.get("year").asText();
        try{
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            Optional<Slot> slot = slotRepository.findById(id);
            double[] money = profitService.getSlotMoneyByDaysInMonth(type,slot.get(), monthInt, yearInt);
            if(money.length > 0){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", money)
                );
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        }catch (NumberFormatException | ParseException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }
    @GetMapping("/slots/{id}/{type}/months-in-year")
    ResponseEntity<ResponseObject> getSlotMoneyByMonthsInYear(@RequestBody JsonNode requestBody, @PathVariable Long id, @PathVariable String type) {
        String year = requestBody.get("year").asText();
        try{
            int yearInt = Integer.parseInt(year);
            Optional<Slot> slot = slotRepository.findById(id);
            double[] money = profitService.getSlotMoneyByMonthsInYear(type,slot.get(), yearInt);
            if(money.length > 0){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", money)
                );
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        }catch (NumberFormatException | ParseException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }

    @GetMapping("/all-branch/{type}/months-in-year")
    ResponseEntity<ResponseObject> getAllBranchMoneyByMonthsInYear(@RequestParam String year, @PathVariable String type) {
        try{
            int yearInt = Integer.parseInt(year);
            double[] money = profitService.getAllBranchsMoneyByMonthsInYear(type, yearInt);
            if(money.length > 0){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", money)
                );
            }else{
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        }catch (NumberFormatException | ParseException e){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Fail", "", ""));
        }

    }

    @GetMapping("/all-branch/{type}/days-in-month")
    ResponseEntity<ResponseObject> getAllBranchMoneyByDaysInMonth(@RequestParam String month, @RequestParam String year, @PathVariable String type) {
        try {
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);
            double[] money = profitService.getAllBranchsMoneyByDaysInMonth(type, monthInt,yearInt);
            if (money.length > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", money)
                );
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Fail", "", ""));
            }

        } catch (NumberFormatException | ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Fail", "", ""));
        }
    }
}
