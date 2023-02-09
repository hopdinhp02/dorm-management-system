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
public class dorm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private branch branch;
    @OneToMany(mappedBy = "dorms", cascade = CascadeType.ALL)
    private Collection<room> rooms;
    @OneToMany(mappedBy = "dorms", cascade = CascadeType.ALL)
    private Collection<booking_request> bookingRequests;
}
