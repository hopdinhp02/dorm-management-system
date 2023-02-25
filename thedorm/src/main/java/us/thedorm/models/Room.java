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
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int floor;
//    @Column(insertable=false, updatable=false)
//    private long dorm_id;
//    private int base_price_id;
 //   private int type_id;
    private int status;
    @ManyToOne
    @JoinColumn(name = "dorm_id")
    private Dorm dorm;
    @ManyToOne
    @JoinColumn(name = "base_price_id")
    private BasePrice basePrice;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ElectricWaterUsage> electricWaterUsages;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Facility> dormFacilities;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Slot> slots;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Facility> facilities;
}
