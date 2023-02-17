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
@Table(name = "profile_image_type")
public class ProfileImageType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int type;
    @OneToMany(mappedBy = "profileImageType", cascade = CascadeType.ALL)
    private Collection<DormImage> dormImages;
    @ManyToOne
    @JoinColumn(name = "dorm_profile_id")
    private DormProfile dormProfile;
}
