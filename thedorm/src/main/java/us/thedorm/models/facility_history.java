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
public class facility_history {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String note;
    private int cost;
    private Date created_date;
    private Date created_by;

    @ManyToOne
    @JoinColumn(name = "status")
    private facility_status facility_status;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private dorm_facility dormFacilitys;
}
