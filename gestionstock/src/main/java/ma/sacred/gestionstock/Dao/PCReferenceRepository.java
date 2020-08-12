package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.MelangeReference;
import ma.sacred.gestionstock.Entities.PCReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PCReferenceRepository extends JpaRepository<PCReference, Long> {
    public Page<PCReference> findByReferenceContains(String keyword, Pageable pageable);
    public PCReference findByReference(String ref);
}
