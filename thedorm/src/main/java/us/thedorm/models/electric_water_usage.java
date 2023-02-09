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
public class electric_water_usage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long room_id;
    private String type;
    private Date from_date;
    private Date to_date;
    private int from_amount;
    private int to_amount;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private room rooms;
}
