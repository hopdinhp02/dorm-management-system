package us.thedorm.models;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history_booking_request")
public class HistoryBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int status;
    private String note;
    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "booking_request_id")
    private BookingRequest bookingRequest;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserInfo userInfo;


}
