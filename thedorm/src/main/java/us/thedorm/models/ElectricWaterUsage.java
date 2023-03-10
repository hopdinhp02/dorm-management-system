package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Date;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "electric_water_usage")
public class ElectricWaterUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long room_id;
//    private String type;
//    @Column(name = "from_date")
//    private Date fromDate;
//    @Column(name = "to_date")
//    private Date toDate;
    @Column(name="monthPay")

    private LocalDate monthPay;
    @Column(name="createdDate")
    private Date createdDate;
    @Column(name ="electricStart")
    private int electricStart;
    @Column(name ="electricEnd")
    private int electricEnd;
    @Column(name ="waterStart")
    private int waterStart;
    @Column(name ="waterEnd")
    private int waterEnd;
    @Column(name ="ElectricUsage")
    private int ElectricUsage;
    @Column(name ="WaterUsage")
    private int WaterUsage;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
