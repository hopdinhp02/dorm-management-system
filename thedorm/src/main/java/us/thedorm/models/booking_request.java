package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    private String note;
    private Date start_date;
    private Date end_date;
    private Date created_date;
    private int status;


    @ManyToOne
    @JoinColumn(name = "resident_id")
    @JsonBackReference// many
    private user_info user_info;
    @ManyToOne
    @JoinColumn(name = "bed_id")
    @JsonBackReference// many
    private bed bed;

    @OneToMany(mappedBy = "booking_request", cascade = CascadeType.ALL)
    @JsonManagedReference//one
    private Collection<history_booking_request> history_booking_requests;
}
