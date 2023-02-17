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
@Table(name = "dorm_profile")
public class DormProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dorm_id")
    private Dorm dorms;
    @ManyToOne
    @JoinColumn(name = "room_price")
    private BasePrice basePrice;
    @OneToMany(mappedBy = "dormProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ProfileImageType> profileImageTypes;
}
