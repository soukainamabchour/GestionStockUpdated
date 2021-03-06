package ma.sacred.gestionstock.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min=1, max=50)
    private String lot;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFabrication;
    @NotNull
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
    @OneToOne
    private Machine machine;
    @OneToOne
    private Fin fin;
}
