package ma.sacred.gestionstock.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Melange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lot;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFabrication;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateReception;
    private LocalDateTime dateUtilisation;
    private double dimension;
    private double poids;
    private long jours;
    @ManyToOne
    private MelangeReference reference;
    @OneToOne
    private MelangeEmplacement emplacement;
}
