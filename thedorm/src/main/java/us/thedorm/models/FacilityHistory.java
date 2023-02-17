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
@Table(name = "facility_history")
public class FacilityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String note;
    private int cost;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "created_by")
    private Date createdBy;

    @ManyToOne
    @JoinColumn(name = "status")
    private FacilityStatus facilityStatus;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private DormFacility dormFacilities;
}
