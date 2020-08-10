package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MelangeReferenceRepository extends JpaRepository<MelangeReference, Long> {
    public Page<MelangeReference> findByReferenceContains(String keyword, Pageable pageable);
    public MelangeReference findByReference(String keyword);
}
