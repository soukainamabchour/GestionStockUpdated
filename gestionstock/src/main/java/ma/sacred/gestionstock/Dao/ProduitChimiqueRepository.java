package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Melange;
import ma.sacred.gestionstock.Entities.ProduitChimique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitChimiqueRepository extends JpaRepository<ProduitChimique, Long> {
    public ProduitChimique findByIdAndReference_Id(Long id, Long ref_id);
    public Page<ProduitChimique> findByReference_IdAndNomContainsOrderByJoursAsc(Long id, String keyword, Pageable page);
    public Page<ProduitChimique> findAllByNomContainsOrderByJoursAsc(String kw, Pageable pageable);
}
