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

public class billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;
    private int cost;
    private String status;

    private Date created_date;
    private Date deadline_date;
    private Date pay_date;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private resident resident;

}
