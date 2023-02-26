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
@Table(name = "CheckInOut")
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
    private UserInfo userInfo;
    @ManyToOne
    @JoinColumn(name = "residentId")
    private UserInfo user_info;
    private Date createDate;
    private Date confirmDate;

    @Enumerated(EnumType.STRING)
    private Type type;
}
