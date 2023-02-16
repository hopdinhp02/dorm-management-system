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
public class dorm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference// many
    private branch branch;
    @OneToMany(mappedBy = "dorms", cascade = CascadeType.ALL)
    @JsonManagedReference// one
    private Collection<room> rooms;



}
