package us.thedorm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int no_beds;
    private int floor;
    @Column(insertable=false, updatable=false)
    private long dorm_id;
//    private int base_price_id;
 //   private int type_id;
    private int status;
    @ManyToOne

    @JoinColumn(name = "dorm_id")

    private dorm dorms;
    @ManyToOne
    @JoinColumn(name = "base_price_id")
    private base_price basePrice;
    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL)
    private Collection<electric_water_usage> electricWaterUsages;
    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL)
    private Collection<dorm_facility> dormFacilities;
    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL)
    private Collection<bed> beds;
}
