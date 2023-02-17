package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String image;


    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<BookingRequest> bookingRequests;
    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<Billing> billings;

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<ResidentHistory> residentHistories;

    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<HistoryBookingRequest> historyBookingRequests;
    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<HistoryBasePrice> historyBasePrices;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
