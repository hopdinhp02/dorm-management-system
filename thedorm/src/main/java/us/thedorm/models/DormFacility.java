package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dorm_facility")
public class DormFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
//    private int type_id;
   // private int status;
//    private long room_id;
 //   private long bed_id;
    private int floor;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room rooms;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private FacilityType facilityType;
    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;
    @OneToMany(mappedBy = "dormFacilities",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<FacilityHistory> facilityHistories;
    @ManyToOne
    @JoinColumn(name = "status")
    private FacilityStatus facilityStatus;

}
