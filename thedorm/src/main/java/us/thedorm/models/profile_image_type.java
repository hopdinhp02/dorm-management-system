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
public class profile_image_type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long dorm_profile_id;
    private int type;
    @OneToMany(mappedBy = "profile_image_type", cascade = CascadeType.ALL)
    private Collection<dorm_image> dormImages;
    @ManyToOne
    @JoinColumn(name = "dorm_profile_id")
    private dorm_profile dormProfile;
}
