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
public class dorm_profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dorm_id")
    private dorm dorms;
    @ManyToOne
    @JoinColumn(name = "room_price")
    private base_price basePrice;
    @OneToMany(mappedBy = "dormProfile", cascade = CascadeType.ALL)
    private Collection<profile_image_type> profileImageTypes;
}
