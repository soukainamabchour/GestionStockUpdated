package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Machine;

import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    public Page<Machine> findByReferenceContains(String ref, Pageable pageable);
    public Machine findByReference(String ref);
    public List<Machine> findByEtatIsFalse();

}
