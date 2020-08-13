package ma.sacred.gestionstock.Dao;

import ma.sacred.gestionstock.Entities.Melange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

public interface MelangeRepository extends JpaRepository<Melange, Long> {

    public Melange findByIdAndReference_Id(Long id, Long ref_id);
    public Page<Melange> findByReference_IdAndLotContainsOrderByJoursAsc(Long id, String keyword, Pageable page);
    public Page<Melange> findAllByLotContainsOrderByJoursAsc(String kw, Pageable pageable);
    public Melange findByLot(String lot);
    public Melange findByLotAndMachine_ReferenceAndDateFabricationAndDateReceptionAndDimensionAndPoids
            (String lot, String ref, LocalDate df, LocalDate dr, double dim, double poids);
    public Melange findByLotAndEmplacement_IdAndDateFabricationAndDateReceptionAndDimensionAndPoids
            (String lot, Long id, LocalDate df, LocalDate dr, double dim, double poids);
}
