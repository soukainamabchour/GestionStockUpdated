package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Composant;
import ma.sacred.gestionstock.Entities.Melange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposantRepository extends JpaRepository<Composant, Long> {
    public Composant findByIdAndReference_Id(Long id, Long ref_id);
    public Page<Composant> findByReference_IdAndNomContainsOrderByJoursAsc(Long id, String keyword, Pageable page);
    public Page<Composant> findAllByNomContainsOrderByJoursAsc(String kw, Pageable pageable);
}
