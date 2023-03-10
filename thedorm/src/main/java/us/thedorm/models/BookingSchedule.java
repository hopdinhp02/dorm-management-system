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
@Table(name = "booking_schedule")
public class BookingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="branch_id")
    private Branch branch;
    private Date keepStartDate;
    private Date keepEndDate;
    private Date newStartDate;
    private Date newEndDate;

    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    private boolean isReset;
    private boolean isActive;
    public void setReset(boolean reset) {
        isReset = reset;
    }
}
