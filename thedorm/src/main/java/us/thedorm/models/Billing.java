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
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    private String type;
@Enumerated(EnumType.STRING)
private TypeBilling typeBilling;
    private int cost;
//    private String status;
    @Enumerated(EnumType.STRING)
    private StatusBilling statusBilling;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "deadline_date")
    private Date deadlineDate;
    @Column(name = "pay_date")
    private Date payDate;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private UserInfo userInfo;

}
