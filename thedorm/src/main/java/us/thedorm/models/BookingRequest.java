package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_request")
public class BookingRequest {
    public enum Status{
        Processing,
        Paying,
        Accept,
        Decline

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String note;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "created_date")
    private Date createdDate;
//    private int status;
@Enumerated(EnumType.STRING)
private Status status;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private UserInfo userInfo;
    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;

    @OneToMany(mappedBy = "bookingRequest", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<HistoryBookingRequest> historyBookingRequests;
}
