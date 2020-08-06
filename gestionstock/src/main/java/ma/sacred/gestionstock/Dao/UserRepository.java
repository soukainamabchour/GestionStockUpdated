package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    public User findByUsername(String username);
    Page<User> findByUsernameContains(String kw, Pageable pageable);
}
