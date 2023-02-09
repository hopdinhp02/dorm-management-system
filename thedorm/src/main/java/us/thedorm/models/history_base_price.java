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
public class history_base_price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private int base_price_id;
    private String type;
    private int price;
    private Date created_date;
    @ManyToOne
    @JoinColumn(name = "base_price_id")
    private base_price basePrice;
    @ManyToOne
    @JoinColumn(name = "UpdateBy")
    private user_info user_info;
}
