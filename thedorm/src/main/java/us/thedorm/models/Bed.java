package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bed")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
   // private long room_id;
    private int status;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room rooms;
    @OneToMany(mappedBy = "bed", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<DormFacility> dormFacilities;
    @OneToMany(mappedBy = "bed", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ResidentHistory> residentHistories;
    @OneToMany(mappedBy = "bed", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<BookingRequest> bookingRequests;
}