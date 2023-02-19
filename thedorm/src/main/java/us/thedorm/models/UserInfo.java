package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")

@Builder
public class UserInfo implements UserDetails {


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
