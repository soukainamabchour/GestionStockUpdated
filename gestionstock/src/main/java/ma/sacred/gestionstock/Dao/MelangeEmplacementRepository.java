package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MelangeEmplacementRepository extends JpaRepository<MelangeEmplacement, Long> {
    public Page<MelangeEmplacement> findById(Long id, Pageable page);
}

