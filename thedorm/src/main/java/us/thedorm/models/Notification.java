package us.thedorm.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="notification")
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name="title")
    private String title;
    @Column(name="content")
    private String content;
    @Column(name="created_date")
    private Date createdDate;
    @ManyToOne
    @Nullable
    @JoinColumn(name = "resident_id")
    private UserInfo userInfo;
}
