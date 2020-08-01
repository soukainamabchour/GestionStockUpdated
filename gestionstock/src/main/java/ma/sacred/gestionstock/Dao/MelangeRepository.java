package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Melange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MelangeRepository extends JpaRepository<Melange, Long> {
   // public Page<Melange> findByReference_IdAndLotContains();
    public Melange findByIdAndReference_Id(Long id, Long ref_id);
    public Page<Melange> findByReference_IdAndLotContainsOrderByJoursAsc(Long id, String keyword, Pageable page);
}
