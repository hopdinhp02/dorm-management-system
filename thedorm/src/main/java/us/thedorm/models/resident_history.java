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

public class resident_history {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long price;
    private Date checkin_date;
    private Date checkout_date;
    private Date start_date;
    private Date end_date;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private user_info user_info;

    @ManyToOne
    @JoinColumn(name = "bed_id")
    private bed beds;
}
