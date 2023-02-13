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

public class bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
   // private long room_id;
    private int status;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private room rooms;
    @OneToMany(mappedBy = "beds", cascade = CascadeType.ALL)
    private Collection<dorm_facility> dormFacilities;
    @OneToMany(mappedBy = "beds", cascade = CascadeType.ALL)
    private Collection<resident_history> residentHistories;
}
