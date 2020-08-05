package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    public User findByUsername(String username);
}
