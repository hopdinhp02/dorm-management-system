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
@Table(name = "history_base_price")
public class HistoryBasePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private int base_price_id;
    private String type;
    private int price;
    @Column(name = "created_date")
    private Date createdDate;
    @ManyToOne
    @JoinColumn(name = "base_price_id")
    private BasePrice basePrice;
    @ManyToOne
    @JoinColumn(name = "UpdateBy")
    private UserInfo userInfo;
}
