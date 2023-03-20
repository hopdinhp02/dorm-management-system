
package us.thedorm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="FacilityDetail")
@Builder

public class FacilityDetail {
    public enum Status{
        good,
        broken,
        irreparable

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String provider;
    private Date producingDate;
    private Date expirationDate;
    private int price;
    private int value;
    private String codeProduct;

   private String type;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "facilityDetail",cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Maintenance> maintenances;


}
