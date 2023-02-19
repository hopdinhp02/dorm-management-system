package us.thedorm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.thedorm.models.ResidentHistory;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;
import us.thedorm.repositories.BillingRepository;
import us.thedorm.repositories.ResidentHistoryRepository;
import us.thedorm.service.BookingService;

import java.util.Date;

@Service
public class BookingServiceImpl implements BookingService {
   @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private ResidentHistoryRepository residentHistoryRepository;


    @Override
    public void insertBilling(TypeBilling typeBilling, int cost, StatusBilling statusBilling, long resident_id) {
        billingRepository.insertBilling(typeBilling,cost,statusBilling,resident_id);
    }

    @Override
    public void insertResidentHistory(long resident_id, long bed_id, Date checkin_date, Date checkout_date) {
residentHistoryRepository.insertResidentHistory(resident_id,bed_id,checkin_date,checkout_date);
    }
}
