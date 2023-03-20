package us.thedorm.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")

@Builder
public class UserInfo implements UserDetails {
    public enum Role {
        ADMIN,
        USER,
        STAFF,
        GUARD

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String image;
    private double balance;
    private boolean isActive;

    private boolean gender;


    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<BookingRequest> bookingRequests;
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Billing> billings;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ResidentHistory> residentHistories;

    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<HistoryBookingRequest> historyBookingRequests;
    @OneToMany(mappedBy = "userInfo",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<HistoryBasePrice> historyBasePrices;
    @OneToMany(mappedBy = "userInfo")
    @JsonIgnore
    private Collection<Notification> notifications;
    @OneToMany(mappedBy = "guard" ,cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<CheckInOut> guardCheck;
    @OneToMany(mappedBy = "resident",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<CheckInOut> residentCheck;
    @OneToMany(mappedBy = "resident",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Request> requests;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
