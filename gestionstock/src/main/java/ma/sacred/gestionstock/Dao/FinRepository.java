package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Fin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FinRepository extends JpaRepository<Fin, Long> {
    public Page<Fin> findByPlaceContains(String place, Pageable page);
    public Fin findByPlace(String pl);
}
