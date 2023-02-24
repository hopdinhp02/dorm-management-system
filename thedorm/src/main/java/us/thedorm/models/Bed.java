package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bed")
@Builder
public class Bed {
    public enum Status {

        Available,

        NotAvailable


    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
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
