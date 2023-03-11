package us.thedorm.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.thedorm.models.*;
import us.thedorm.repositories.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1/elec-water-usages")
public class ElectricWaterUsageController {
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;
    @Autowired
    private ElectricWaterUsageRepo electricWaterUsageRepo;
    @Autowired
    private SlotRepository slotRepository;
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllUsage() {
        List<ElectricWaterUsage> found = electricWaterUsageRepo.findAll();
        if (found.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "", found)
        );

    }


    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<ElectricWaterUsage> found =electricWaterUsageRepo.findById(id);
        return found.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "", found)
        ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }


    @PostMapping("")
    ResponseEntity<ResponseObject> insert(@RequestBody ElectricWaterUsage newElectricWaterUsage) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert successfully", electricWaterUsageRepo.save(newElectricWaterUsage))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> update(@RequestBody ElectricWaterUsage newElectricWaterUsage, @PathVariable Long id) {
        ElectricWaterUsage update = electricWaterUsageRepo.findById(id)
                .map(electricWaterUsage -> {
//                    electricWaterUsage.setType(newElectricWaterUsage.getType());
//                    electricWaterUsage.setFromDate(newElectricWaterUsage.getFromDate());
//                    electricWaterUsage.setToDate(newElectricWaterUsage.getToDate());
//                    electricWaterUsage.setFromAmount(newElectricWaterUsage.getFromAmount());
//                    electricWaterUsage.setToAmount(newElectricWaterUsage.getToAmount());
                    electricWaterUsage.setElectricStart(newElectricWaterUsage.getElectricStart());
                    electricWaterUsage.setElectricEnd(newElectricWaterUsage.getElectricEnd());
                    electricWaterUsage.setWaterStart(newElectricWaterUsage.getWaterStart());
                    electricWaterUsage.setWaterEnd(newElectricWaterUsage.getWaterEnd());
                    electricWaterUsage.setRoom(newElectricWaterUsage.getRoom());
                    electricWaterUsage.setElectricUsage(newElectricWaterUsage.getElectricEnd() - newElectricWaterUsage.getElectricStart());
                    electricWaterUsage.setWaterUsage(newElectricWaterUsage.getWaterEnd() - newElectricWaterUsage.getWaterStart());
                    return electricWaterUsageRepo.save(electricWaterUsage);

                }).orElseGet(() -> null);
        if (update != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "Insert Product successfully", update)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "", ""
                ));
    }

        @DeleteMapping("/{id}")
        ResponseEntity<ResponseObject> delete(@PathVariable Long id) {

            boolean exists = electricWaterUsageRepo.existsById(id);
            if (exists) {
                electricWaterUsageRepo.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", "")
            );
        }
    @PutMapping("/{roomId}/write-electric-water-usage")
    public ResponseEntity<?> writeElectricWaterUsage(@PathVariable Long roomId, @RequestBody ElectricWaterUsage electricWaterUsage) throws ParseException {

        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        List<Slot> slots = slotRepository.getSlotsByRoom_IdAndStatus(roomId, Slot.Status.NotAvailable);
        if (slots.isEmpty()) {
            return ResponseEntity.badRequest().body("Không có người ở trong phòng");
        }
        int month = electricWaterUsage.getMonthPay().getMonthValue();
        int year = electricWaterUsage.getMonthPay().getYear();

        List<ElectricWaterUsage> ListElectric = electricWaterUsageRepo.ListElecWaterOfRoomIdInMonth(month,year,roomId);
                if(ListElectric.size() != 0){
                    return ResponseEntity.badRequest().body("Đã ghi điện nước");
                }
                electricWaterUsage.setCreatedDate(new Date());
                electricWaterUsage.setRoom(roomRepository.findById(roomId).orElse(null));
                electricWaterUsage.setElectricUsage(electricWaterUsage.getElectricEnd()-electricWaterUsage.getElectricStart());
                electricWaterUsage.setWaterUsage(electricWaterUsage.getWaterEnd()- electricWaterUsage.getWaterStart());
                electricWaterUsageRepo.save(electricWaterUsage);
        LocalDate monthPay = electricWaterUsage.getMonthPay();

        List<ResidentHistory> Lists = residentHistoryRepository.findResidentsByRoomIdInMonth(roomId,monthPay);
        for (ResidentHistory resident:Lists)
        {
           Long residentId = resident.getUserInfo().getId();
            Billing newElecBilling = Billing.builder()
                    .userInfo(UserInfo.builder().id(residentId).build())
                    .type(Billing.Type.Electric)
                    .cost((electricWaterUsage.getElectricUsage()*3000)/Lists.size())
                    .status(Billing.Status.Unpaid)
                    .createdDate(new Date())
                    .build();
            billingRepository.save(newElecBilling);

            Billing newWaterBilling = Billing.builder()
                    .userInfo(UserInfo.builder().id(residentId).build())
                    .type(Billing.Type.Water)
                    .cost((electricWaterUsage.getWaterUsage()*5000)/Lists.size())
                    .status(Billing.Status.Unpaid)
                    .createdDate(new Date())
                    .build();
            billingRepository.save(newWaterBilling);
        }



        return ResponseEntity.ok().build().badRequest().body(" ghi số điện nươc thành công!");
    }
    // toàn bộ record lương điện nước của 1 phòng được ghi lại
    @GetMapping("/{roomId}/view-electric-water-usage")
    public ResponseEntity<ResponseObject> ListElectricWaterUsage(@PathVariable Long roomId){
        List<ElectricWaterUsage> ListElectricWaterUsage = electricWaterUsageRepo.findElectricWaterUsagesByRoomId(roomId);
        if(ListElectricWaterUsage.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "", ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", " List Resident By RoomId", ListElectricWaterUsage));
    }
    //
    @GetMapping("/view-list-Elec-water-of-room/{roomid}/and-resident/{residentId}-in-month-by-bill/{ewuId}")
    ResponseEntity<ResponseObject> getListResidentsOfRoomInMonth(@RequestBody JsonNode requestBody, @PathVariable Long roomid, @PathVariable Long ewuId,@PathVariable Long residentId) throws ParseException {
        String monthRaw = requestBody.get("month").asText();
        String yearRaw = requestBody.get("year").asText();
        try{
            int month = Integer.parseInt(monthRaw);
            int year = Integer.parseInt(yearRaw);
            ResidentHistory ResidentsOfRoomInMonth = electricWaterUsageRepo.ElecWaterOfRoomIdAndResidentInMonth(month,year,roomid,ewuId,residentId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", "List Residents  of Room in month", ResidentsOfRoomInMonth)
            );
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Fail", "", ""));
        }
    }
    @GetMapping("/all-electric-water-usage-of-resident/{residentId}")
    ResponseEntity<ResponseObject> GetListElectricWaterUsageOfResident(@PathVariable Long residentId)
    {
        List<ElectricWaterUsage> ListElectricWaterUsageOfResidentId = electricWaterUsageRepo.ListElecWaterOfResidenId(residentId);
        if(ListElectricWaterUsageOfResidentId.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Fail","",""));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "", ListElectricWaterUsageOfResidentId)
        );

    }

    }



