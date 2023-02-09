package us.thedorm.models;

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

public class booking_request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int floor;
    private String note;
    private long price;
    private Date start_date;
    private Date end_date;
    private Date created_date;
    private int status;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private user_info user_info;

    @ManyToOne
    @JoinColumn(name = "dorm_id")
    private dorm dorm;

    @OneToMany(mappedBy = "booking_request", cascade = CascadeType.ALL)
    private Collection<history_booking_request> history_booking_requests;
}
