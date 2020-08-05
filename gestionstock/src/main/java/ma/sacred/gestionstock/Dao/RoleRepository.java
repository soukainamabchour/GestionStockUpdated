package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    public Role findByRole(String role);
}
