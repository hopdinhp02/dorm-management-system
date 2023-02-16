package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class base_price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int bed_price;
    private int electric_price;
    private int water_price;
    private int internetPrice;
    private int cleanPrice;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<room> rooms;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    private Collection<dorm_profile> dormProfiles;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    private Collection<history_base_price> historyBasePrices;
}
