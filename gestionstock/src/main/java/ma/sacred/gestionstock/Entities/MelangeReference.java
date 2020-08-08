package ma.sacred.gestionstock.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MelangeReference implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Veuillez Ajouter une référence")
    @Column(unique = true)
    @Size(min=1, message = "Veuillez Ajouter une référence")
    private String reference;
    @OneToMany(mappedBy = "reference")
    private Collection<Melange> melanges;
}
