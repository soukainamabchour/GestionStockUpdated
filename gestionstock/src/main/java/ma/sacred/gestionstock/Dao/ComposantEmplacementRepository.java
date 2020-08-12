package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.ComposantEmplacement;
import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sun.security.mscapi.CPublicKey;

import java.util.List;

public interface ComposantEmplacementRepository extends JpaRepository<ComposantEmplacement, Long> {
    public List<ComposantEmplacement> findByEtatIsFalse();
    public Page<ComposantEmplacement> findByEmplacementContainsOrderByEmplacement(String kw, Pageable pageable);
    public ComposantEmplacement findByEmplacement(String emp);
}
