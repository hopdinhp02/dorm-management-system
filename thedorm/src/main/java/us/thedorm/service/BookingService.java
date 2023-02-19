package us.thedorm.service;

import org.springframework.stereotype.Service;
import us.thedorm.models.Billing;
import us.thedorm.models.HistoryBookingRequest;
import us.thedorm.models.StatusBilling;
import us.thedorm.models.TypeBilling;
import us.thedorm.repositories.BillingRepository;

import java.util.Date;


public interface BookingService {
 // insert  billing
 void insertBilling(TypeBilling typeBilling, int cost, StatusBilling statusBilling, long resident_id);
// insert history booking request
void insertResidentHistory(long resident_id, long bed_id, Date checkin_date, Date checkout_date);

}
