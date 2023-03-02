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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Collection<UserRole> userRoles;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Collection<RolePermission> rolePermissions;
}
