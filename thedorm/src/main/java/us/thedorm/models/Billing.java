package us.thedorm.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "billing")
@Builder
public class Billing {
public enum Status{
    Paid,Unpaid,Refund
}
public enum Type{
    slot, Water,Electric, Internet, Clean
}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    //    private String type;
@Enumerated(EnumType.STRING)
private Type type;
    private int cost;
//    private String status;
@Column(name = "usageAmount")
    private int usageAmount;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "deadline_date")
    private Date deadlineDate;
    @Column(name = "pay_date")
    private Date payDate;
    @Column(name = "refund_date")
    private Date RefundDate;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private UserInfo userInfo;
    private String note;

}
