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
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id
    @NotNull
    @Size(min=1,message = "Le nom d'utilisateur doit être entre 4 et 10 caractères")
    @Column(unique = true)
    private String username;
    @NotNull
    @Size(min=5,  message = "Le mot de passe doit être entre 5 et 16 caractères ")
    private String password;
    private String nom;
    private String prenom;
    private String profession;
    private boolean active;
    @ManyToMany
    @JoinTable(name = "users_roles")
    private Collection<Role> roles;
}
