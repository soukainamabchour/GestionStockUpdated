package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import ma.sacred.gestionstock.Entities.PCEmplacement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PCEmplacementRepository extends JpaRepository<PCEmplacement, Long> {
    public List<PCEmplacement> findByEtatIsFalse();
    public Page<PCEmplacement> findByEmplacementContainsOrderByEmplacement(String kw, Pageable pageable);
}
