package us.thedorm.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resident_history")
@Builder
public class ResidentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "checkin_date")
    private Date checkinDate;
    @Column(name = "checkout_date")
    private Date checkoutDate;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;
}
