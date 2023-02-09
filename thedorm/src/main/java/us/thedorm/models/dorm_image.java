package us.thedorm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class dorm_image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long profile_image_id;
    private String source;
    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private profile_image_type profile_image_type;
}
