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
@Table(name = "base_price")
public class BasePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "bed_price")
    private int bedPrice;
    @Column(name = "electric_price")
    private int electricPrice;
    @Column(name = "water_price")
    private int waterPrice;
    @Column(name = "internet_price")
    private int internetPrice;
    @Column(name = "clean_price")
    private int cleanPrice;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Room> rooms;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<DormProfile> dormProfiles;
    @OneToMany(mappedBy = "basePrice", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<HistoryBasePrice> historyBasePrices;
}
