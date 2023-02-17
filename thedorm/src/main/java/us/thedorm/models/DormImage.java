package us.thedorm.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dorm_image")
public class DormImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private long profile_image_id;
    private String source;
    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImageType profileImageType;
}
