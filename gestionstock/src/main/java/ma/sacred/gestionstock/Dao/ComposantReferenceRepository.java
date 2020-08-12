package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.ComposantReference;
import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposantReferenceRepository extends JpaRepository<ComposantReference, Long> {
    public Page<ComposantReference> findByReferenceContains(String keyword, Pageable pageable);
    public ComposantReference findByReference(String ref);
}
