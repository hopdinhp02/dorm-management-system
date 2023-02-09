package us.thedorm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String image;


    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private Collection<booking_request> booking_requests;
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private Collection<billing> billings;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private Collection<resident_history> resident_histories;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private Collection<history_booking_request> history_booking_requests;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private role role;

}
