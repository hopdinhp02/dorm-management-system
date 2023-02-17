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
@Table(name = "facility_status")
public class FacilityStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "facilityStatus", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<FacilityHistory> facilityHistories;
    @OneToMany(mappedBy = "facilityStatus", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<DormFacility> dormFacilities;


}
