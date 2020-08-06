package ma.sacred.gestionstock.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String profession;
    private boolean active;
    @ManyToMany
    @JoinTable(name = "users_roles")
    private Collection<Role> roles;
}
