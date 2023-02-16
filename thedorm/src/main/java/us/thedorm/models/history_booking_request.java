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
public class history_booking_request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int status;
    private String note;
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "booking_request_id")
    private booking_request booking_request;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private user_info user_info;


}
