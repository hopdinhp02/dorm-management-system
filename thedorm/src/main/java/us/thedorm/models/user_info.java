package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class user_info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String image;


    @OneToMany(mappedBy = "user_info", cascade = CascadeType.ALL)
    @JsonBackReference//one
    private Collection<booking_request> booking_requests;
    @OneToMany(mappedBy = "user_info", cascade = CascadeType.ALL)
    private Collection<billing> billings;

    @OneToMany(mappedBy = "user_info", cascade = CascadeType.ALL)
    private Collection<resident_history> resident_histories;

    @OneToMany(mappedBy = "user_info", cascade = CascadeType.ALL)
    private Collection<history_booking_request> history_booking_requests;
    @OneToMany(mappedBy = "user_info", cascade = CascadeType.ALL)
    private Collection<history_base_price> historyBasePrices;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private role role;

}
