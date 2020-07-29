package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MelangeReferenceRepository extends JpaRepository<MelangeReference, Long> {
    public Page<MelangeReference> findByReferenceContains(String keyword, Pageable pageable);
}
