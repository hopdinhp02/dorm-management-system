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

public class bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
   // private long room_id;
    private int status;
    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference// many
    private room rooms;
    @OneToMany(mappedBy = "beds", cascade = CascadeType.ALL)
    @JsonManagedReference//one
    private Collection<dorm_facility> dormFacilities;
    @OneToMany(mappedBy = "beds", cascade = CascadeType.ALL)
    @JsonManagedReference//one
    private Collection<resident_history> residentHistories;
    @OneToMany(mappedBy = "bed", cascade = CascadeType.ALL)
    @JsonBackReference//one
    private Collection<booking_request> bookingRequests;
}
