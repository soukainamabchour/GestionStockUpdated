package ma.sacred.gestionstock.Services;

import ma.sacred.gestionstock.Dao.MelangeEmplacementRepository;
import ma.sacred.gestionstock.Dao.MelangeReferenceRepository;
import ma.sacred.gestionstock.Dao.MelangeRepository;
import ma.sacred.gestionstock.Entities.Melange;
import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Transactional
@Service
public class StockInitServiceImpl implements IStockInitService {

    @Autowired
    private MelangeEmplacementRepository melangeEmplacementRepository;
    @Autowired
    private MelangeReferenceRepository melangeRefRepository;
    @Autowired
    private MelangeRepository melangeRepository;

    @Override
    public void initMelangeEmplacement() {
        Stream.of("A1", "A2", "A3", "A4", "A5", "A6", "A7").forEach(e -> {
            MelangeEmplacement emplacement = new MelangeEmplacement();
            emplacement.setEmplacement(e);
            emplacement.setEtat(false);
            melangeEmplacementRepository.save(emplacement);
        });
    }

    @Override
    public void initMelangeRef() {
        Stream.of("EPDM 8060", "EPDM 8052", "EPDM 868060", "EPDM 88423B", "EPDM 8370", "EPDM 862363",
                "EPDM 8374", "EPDM 8060GLI", "EPDM 8162", "EPDM 8760").forEach(v -> {
            MelangeReference melangeRef = new MelangeReference();
            melangeRef.setReference(v);
            melangeRefRepository.save(melangeRef);
        });
    }

    @Override
    public void initMelange() {
       /* Melange melange1 = new Melange();
        MelangeReference reference = melangeRefRepository.findById((long) 1).get();
        melange1.setReference(reference);
        MelangeEmplacement emplacement = melangeEmplacementRepository.findById((long) 1).get();
        emplacement.setEtat(true);
        melange1.setEmplacement(emplacement);
        melange1.setLot("LOT 1");
        melange1.setDateReception(LocalDate.now());
        melange1.setDateFabrication(LocalDate.now());
        melange1.setDateUtilisation(LocalDateTime.now());
        melange1.setDimension(50);
        melange1.setPoids(10);
        melangeRepository.save(melange1);

        Melange melange2 = new Melange();
        MelangeReference reference2 = melangeRefRepository.findById((long) 2).get();
        melange1.setReference(reference2);
        MelangeEmplacement emplacement2 = melangeEmplacementRepository.findById((long) 2).get();
        melange1.setEmplacement(emplacement2);
        melangeRepository.save(melange2);*/


    }


    /*@Override
    public void initMelangeRef() {
        Stream.of("EPDM 8060", "EPDM 8052", "EPDM 868060", "EPDM 88423B", "EPDM 8370", "EPDM 862363",
                "EPDM 8374", "EPDM 8060GLI", "EPDM 8162", "EPDM 8760").forEach(v -> {
            MelangeReference melangeRef = new MelangeReference();
            melangeRef.setReference(v);
            melangeRefRepository.save(melangeRef);
        });
    }

    @Override
    public void initMelange() {
        for (int i = 0; i < 10; i++) {
            int finalI = i+1;
            int j=i;
            melangeRefRepository.findAll().forEach(melangeRef -> {
                Melange melange = new Melange();
                melange.setReference(melangeRef);
                melange.setDateReception(new Date());
                melange.setDateFabrication(new Date());
                melange.setDateUtilisation(new Date());
                melange.setDimension(50);
                melange.setPoids(10);
                melange.setLot("lot"+ finalI);
                melangeRepository.save(melange);
            });
        }
    }

    @Override
    public void initMelangeEmplacement() {
        *//*MelangeRef melangeRef = melangeRefRepository.findById((long) 1).get();
        for (int i=0; i<2; i++)
        {
            MelangeEmplacement emplacement=new MelangeEmplacement();
            emplacement.setReference(melangeRef);
            emplacement.setEmplacement("Emp"+i+1);
            melangeRef.getEmplacements().add(emplacement);
            melangeEmplacementRepository.save(emplacement);
        }*//*
        melangeRefRepository.findAll().forEach(ref -> {
           for (int i=0; i<5; i++){
               int j=i+1;
               MelangeEmplacement melangeEmplacement = new MelangeEmplacement();
               melangeEmplacement.setEmplacement("Emp"+j);
               melangeEmplacement.setEtat(false);
               melangeEmplacementRepository.save(melangeEmplacement);
          }
        });
        *//*for (int i=0; i<2; i++){
            MelangeRef ref=melangeRefRepository.findById((long) 1).get();
            emplacement.setEmplacement("Emp"+i+1);
            emplacement.setReference(ref);
            melangeEmplacementRepository.save(emplacement);
        }
        for (int i=2; i<4; i++){
            MelangeRef ref=melangeRefRepository.findById((long) 2).get();
            MelangeEmplacement emplacement=new MelangeEmplacement();
            emplacement.setEmplacement("Emp"+i+1);
            emplacement.setReference(ref);
            melangeEmplacementRepository.save(emplacement);
        }
        for (int i=4; i<6; i++){
            MelangeRef ref=melangeRefRepository.findById((long) 3).get();
            MelangeEmplacement emplacement=new MelangeEmplacement();
            emplacement.setEmplacement("Emp"+i+1);
            emplacement.setReference(ref);
            melangeEmplacementRepository.save(emplacement);
        }
        for (int i=6; i<8; i++){
            MelangeRef ref=melangeRefRepository.findById((long) 4).get();
            MelangeEmplacement emplacement=new MelangeEmplacement();
            emplacement.setEmplacement("Emp"+i+1);
            emplacement.setReference(ref);
            melangeEmplacementRepository.save(emplacement);
        }

    }*/

}
