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
@Table(name = "electric_water_usage")
public class ElectricWaterUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long room_id;
    private String type;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "to_date")
    private Date toDate;
    @Column(name = "from_amount")
    private int fromAmount;
    @Column(name = "to_amount")
    private int toAmount;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room rooms;
}
