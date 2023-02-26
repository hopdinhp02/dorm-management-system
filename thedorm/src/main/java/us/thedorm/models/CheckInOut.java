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
@Table(name = "check_in_out")
public class CheckInOut {
    public enum Type{
        checkin,
        checkout
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "guardId")
    private UserInfo guard;
    @ManyToOne
    @JoinColumn(name = "residentId")
    private UserInfo resident;
    private Date createDate;
    private Date confirmDate;
    private Type type;
}
