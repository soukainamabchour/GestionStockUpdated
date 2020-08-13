package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.MachineRepository;
import ma.sacred.gestionstock.Dao.MelangeEmplacementRepository;
import ma.sacred.gestionstock.Dao.MelangeReferenceRepository;
import ma.sacred.gestionstock.Dao.MelangeRepository;
import ma.sacred.gestionstock.Entities.Machine;
import ma.sacred.gestionstock.Entities.Melange;
import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MelangeController {

    @Autowired
    MelangeReferenceRepository melangeReferenceRepository;

    @Autowired
    MelangeRepository melangeRepository;

    @Autowired
    MelangeEmplacementRepository melangeEmplacementRepository;

    @Autowired
    MachineRepository machineRepository;


    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String homepage() {
        return "homepage";
    }

    @RequestMapping(value = "/notAuthorized", method = RequestMethod.POST)
    public String notAuthorized() {
        return "notAuthorized";
    }

    @RequestMapping(value = "/melanges", method = RequestMethod.GET)
    public String melanges() {
        return "Melanges";
    }

    /////////////////----------------------Reference Melange-----------------------////////////////
    ////////---------------Lister références------------///////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(path = "/melangeRef")
    public String listRefMelange(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<MelangeReference> melangesref = melangeReferenceRepository.findByReferenceContains(kw, PageRequest.of(p, s));
        model.addAttribute("melangeref", melangesref.getContent());
        model.addAttribute("pages", new int[melangesref.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", s);
        model.addAttribute("keyword", kw);
        return "listMelangeRef";
    }

    ////////------------------Ajouter référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formMelangeRef", method = RequestMethod.GET)
    public String formMelangeRef(Model model) {
        model.addAttribute("melangeRef", new MelangeReference());
        return "formMelangeRef";
    }


    ////////------------------Enregistrer référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/addMelangeRef")
    public String addMelangeRef(@Valid MelangeReference melangeRef, BindingResult bindingResult, Model model) {
        MelangeReference reference = melangeReferenceRepository.findByReference(melangeRef.getReference());
        if (reference == null) {
            model.addAttribute("melangeRef", melangeRef);
            if (bindingResult.hasErrors()) return "formMelangeRef";
            melangeReferenceRepository.save(melangeRef);
            return "saveMelangeRef";
        } else {
            model.addAttribute("message", "lot existe");
            return "existe";
        }
    }

    ////////------------------Modifier référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editMelangeRef", method = RequestMethod.GET)
    public String editMelangeRef(Model model, Long id) {
        MelangeReference melangeRef = melangeReferenceRepository.findById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("melangeRef", melangeRef);
        return "formMelangeRef";
    }

    ////////------------------Supprimer référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteMelangeRef", method = RequestMethod.POST)
    public String deleteMelangeRef(Long id, int page, int size) {
        melangeReferenceRepository.deleteById(id);
        return "redirect:/melangeRef?page=" + page + "&size=" + size + "";
    }

    /////////////////---------------------- Melange-----------------------////////////////
    /////////////////////////////////-----Lister tous les mélanges----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listerMelanges", method = GET)
    public String listerMelanges(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "") String kw
    ) {
        Page<Melange> melanges = melangeRepository.findAllByLotContainsOrderByJoursAsc(kw, PageRequest.of(p, s));
        model.addAttribute("result", melanges.getTotalElements());
        model.addAttribute("listMelange", melanges.getContent());
        model.addAttribute("pages", new int[melanges.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("keyword", kw);
        return "listerMelanges";
    }


    /////////////////////////////////-----Retirer----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/useMelanges", method = RequestMethod.POST)
    public String useMelanges(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "id") Long id) {
        Melange melange = melangeRepository.findById(id).get();
        List<Machine> machine = machineRepository.findByEtatIsFalse();
        model.addAttribute("machines", machine);
        model.addAttribute("melange", melange);
        return "useMelanges";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/melangeMachines", method = POST)
    public String melangeMachines(Model model, String lot, String reference) {
        Melange melange = melangeRepository.findByLot(lot);
        Machine machine = machineRepository.findByReference(reference);
        Long emp = melange.getEmplacement().getId();
        MelangeEmplacement old_emp = melangeEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        melange.setEmplacement(null);
        melange.setDateUtilisation(LocalDateTime.now());
        machine.setEtat(true);
        melange.setMachine(machine);;
        melangeEmplacementRepository.save(old_emp);
        machineRepository.save(machine);
        melangeRepository.save(melange);
        return "redirect:/listerMelanges";
    }

    /////////////////////////////////-----Ajouter----------////////////////////////////////

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formMelanges", method = RequestMethod.GET)
    public String formMelanges(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int p,
                               @RequestParam(name = "size", defaultValue = "5") int s) {
        List<MelangeReference> references = melangeReferenceRepository.findAll();
        List<MelangeEmplacement> emplacements = melangeEmplacementRepository.findByEtatIsFalse();
        Melange melange = new Melange();
        model.addAttribute("melange", melange);
        model.addAttribute("emplacement", emplacements);
        model.addAttribute("references", references);
        return "formMelanges";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addMelanges", method = RequestMethod.POST)
    public String addMelanges(@Valid Melange melange, BindingResult br, Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s) {
        Melange melange1 = melangeRepository.findByLot(melange.getLot());
        if (melange1 == null) {
            if (br.hasErrors()) return "formMelanges";
            melange.getEmplacement().setEtat(true);
            melange.setJours(90 - ChronoUnit.DAYS.between(melange.getDateFabrication(), LocalDate.now()));
            model.addAttribute("melange", melange);
            melangeRepository.save(melange);
            return "saveMelanges";
        }
        return "formMelanges";
    }

    /////////////////////////////////-----Lister  mélanges par ref----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listMelange", method = GET)
    public String listMelange(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long ref_id,
                              @RequestParam(name = "ref") String ref,
                              @RequestParam(name = "keyword", defaultValue = "") String kw
    ) {
        Page<Melange> melange = melangeRepository.findByReference_IdAndLotContainsOrderByJoursAsc(ref_id, kw, PageRequest.of(p, s));
        model.addAttribute("result", melange.getTotalElements());
        model.addAttribute("listMelange", melange.getContent());
        model.addAttribute("pages", new int[melange.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("ref", ref);
        model.addAttribute("keyword", kw);
        return "listMelange";
    }

    ////////------------------Utiliser mélange------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/useMelange", method = RequestMethod.POST)
    public String useMelange(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name = "id") Long id,
                             @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Melange melange = melangeRepository.findById(id).get();
        List<Machine> machine = machineRepository.findByEtatIsFalse();
        model.addAttribute("machines", machine);
        model.addAttribute("melange", melange);
        return "useMelange";


    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/melangeMachine", method = POST)
    public String melangeMachine(Model model, String lot, String reference) {
        Melange melange = melangeRepository.findByLot(lot);
        Machine machine = machineRepository.findByReference(reference);
        Long emp = melange.getEmplacement().getId();
        MelangeEmplacement old_emp = melangeEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        melange.setEmplacement(null);
        melange.setDateUtilisation(LocalDateTime.now());
        machine.setEtat(true);
        melange.setMachine(machine);
        Long ref_id = melange.getReference().getId();
        String ref = melange.getReference().getReference();
        melangeEmplacementRepository.save(old_emp);
        machineRepository.save(machine);
        melangeRepository.save(melange);
        return "redirect:/listMelange?ref_id=" + ref_id + "&ref=" + ref + "";
    }


    ////////------------------Modifier mélange------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editMelange", method = RequestMethod.GET)
    public String editMelange(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long ref_id,
                              @RequestParam(name = "ref") String ref) {
        Melange melange = melangeRepository.findByIdAndReference_Id(id, ref_id);
        Long emp = melange.getEmplacement().getId();
        MelangeEmplacement old_emp = melangeEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        melangeEmplacementRepository.save(old_emp);
        List<MelangeEmplacement> emplacements = melangeEmplacementRepository.findByEtatIsFalse();
        model.addAttribute("melange", melange);
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("emplacement", emplacements);
        return "formEditMelange";
    }


    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editMelangeMachine", method = RequestMethod.GET)
    public String editMelangeMachine(Model model, Long id,
                                     @RequestParam(name = "page", defaultValue = "0") int p,
                                     @RequestParam(name = "size", defaultValue = "5") int s,
                                     @RequestParam(name = "ref_id") Long ref_id,
                                     @RequestParam(name = "ref") String ref,
                                     @RequestParam(name = "machine") String machine_ref) {
        Melange melange = melangeRepository.findByIdAndReference_Id(id, ref_id);
        Machine machine = machineRepository.findByReference(machine_ref);
        Long m=melange.getMachine().getId();
        Machine old_m=machineRepository.findById(m).get();
        old_m.setEtat(false);
        machineRepository.save(old_m);
        List<Machine> machines = machineRepository.findByEtatIsFalse();
        model.addAttribute("melange", melange);
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("machines", machines);
        return "formMelangeMachine";
    }


    ////////------------------Ajouter mélange------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formMelange", method = RequestMethod.GET)
    public String formMelange(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long id,
                              @RequestParam(name = "ref") String ref) {
        List<MelangeEmplacement> emplacements = melangeEmplacementRepository.findByEtatIsFalse();
        Melange melange = new Melange();
        MelangeReference reference = melangeReferenceRepository.findById(id).get();
        melange.setReference(reference);
        model.addAttribute("melange", melange);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        model.addAttribute("emplacement", emplacements);
        return "formMelange";
    }

   /* @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formMelangeMachine", method = RequestMethod.GET)
    public String formMelangeMachine(Model model,
                                     @RequestParam(name = "page", defaultValue = "0") int p,
                                     @RequestParam(name = "size", defaultValue = "5") int s,
                                     @RequestParam(name = "ref_id") Long id,
                                     @RequestParam(name = "ref") String ref) {
        List<Machine> machines = machineRepository.findByEtatIsFalse();
        Melange melange = new Melange();
        MelangeReference reference = melangeReferenceRepository.findById(id).get();
        melange.setReference(reference);
        model.addAttribute("melange", melange);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        model.addAttribute("machines", machines);
        return "formMelangeMachine";
    }*/

    ////////------------------Enregistrer mélange------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/EditMelange", method = RequestMethod.POST)
    public String EditMelange(@Valid Melange melange, BindingResult br, Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name = "ref_id") Long id,
                             @RequestParam(name = "ref") String ref) {
        Melange melange1 = melangeRepository.findByLotAndEmplacement_IdAndDateFabricationAndDateReceptionAndDimensionAndPoids
                (melange.getLot(), melange.getEmplacement().getId(), melange.getDateFabrication(),
                        melange.getDateReception(), melange.getDimension(), melange.getPoids());
        if(melange1==null){
            if (br.hasErrors()) return "formMelange";
            MelangeReference reference = melangeReferenceRepository.findById(id).get();
            melange.getEmplacement().setEtat(true);
            melange.setJours(90 - ChronoUnit.DAYS.between(melange.getDateFabrication(), LocalDate.now()));
            melange.setDateUtilisation(LocalDateTime.now());
            model.addAttribute("melange", melange);
            model.addAttribute("ref_id", id);
            model.addAttribute("ref", ref);
            melange.setReference(reference);
            melangeRepository.save(melange);
            return "saveMelange";
        } else return "formEditMelange";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addMelange", method = RequestMethod.POST)
    public String addMelange(@Valid Melange melange, BindingResult br, Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long id,
                              @RequestParam(name = "ref") String ref) {
        Melange melange1 = melangeRepository.findByLot(melange.getLot());
        if (melange1 == null) {
            if (br.hasErrors()) return "formMelange";
            MelangeReference reference = melangeReferenceRepository.findById(id).get();
            melange.getEmplacement().setEtat(true);
            melange.setJours(90 - ChronoUnit.DAYS.between(melange.getDateFabrication(), LocalDate.now()));
            melange.setDateUtilisation(LocalDateTime.now());
            model.addAttribute("melange", melange);
            model.addAttribute("ref_id", id);
            model.addAttribute("ref", ref);
            melange.setReference(reference);
            melangeRepository.save(melange);
            return "saveMelange";
        } else return "formEditMelange";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addMelangeMachine", method = RequestMethod.POST)
    public String addMelangeMachine(@Valid Melange melange, BindingResult br, Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int p,
                                    @RequestParam(name = "size", defaultValue = "5") int s,
                                    @RequestParam(name = "ref_id") Long id,
                                    @RequestParam(name = "ref") String ref) {
        Melange melange1 = melangeRepository.findByLotAndMachine_ReferenceAndDateFabricationAndDateReceptionAndDimensionAndPoids
                (melange.getLot(), melange.getMachine().getReference(), melange.getDateFabrication(),
                        melange.getDateReception(), melange.getDimension(), melange.getPoids());
        if (melange1 == null) {
            if (br.hasErrors()) return "formMelangeMachine";
            MelangeReference reference = melangeReferenceRepository.findById(id).get();
            melange.getMachine().setEtat(true);
            melange.setJours(90 - ChronoUnit.DAYS.between(melange.getDateFabrication(), LocalDate.now()));
            melange.setDateUtilisation(LocalDateTime.now());
            model.addAttribute("melange", melange);
            model.addAttribute("ref_id", id);
            model.addAttribute("ref", ref);
            melange.setReference(reference);
            melangeRepository.save(melange);
            return "saveMelange";
        } else return "formMelangeMachine";
    }


    ////////------------------Supprimer mélange------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteMelange", method = RequestMethod.POST)
    public String deleteMelange(Model model, Long id,
                                @RequestParam(name = "ref_id") Long ref_id,
                                @RequestParam(name = "ref") String ref,
                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                int page, int size) {
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        Melange melange = melangeRepository.findById(id).get();
        if (melange.getMachine() != null) {
            melange.getMachine().setEtat(false);
        }
        if (melange.getEmplacement() != null) {
            melange.getEmplacement().setEtat(false);
        }
        melangeRepository.deleteById(id);
        return "redirect:/listMelange?ref_id=" + ref_id + "&ref=" + ref + "&page=" + page + "&size=" + size + "&keyword=" + keyword + "";
    }
    /////////////////---------------------- Emplacement Melange-----------------------////////////////

    /////////////////////////////////-----Lister les emplacements----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listEmplacements", method = GET)
    public String listEmplacements(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int p,
                                   @RequestParam(name = "size", defaultValue = "5") int s,
                                   @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<MelangeEmplacement> emplacements = melangeEmplacementRepository.findByEmplacementContainsOrderByEmplacement(kw, PageRequest.of(p, s));
        model.addAttribute("result", emplacements.getTotalElements());
        model.addAttribute("emplacements", emplacements.getContent());
        model.addAttribute("pages", new int[emplacements.getTotalPages()]);
        model.addAttribute("keyword", kw);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        return "listEmplacements";
    }

    /////////////////////////////////-----Ajouter emplacement----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/formMelangeEmp", method = GET)
    public String formMelangeEmp(Model model) {
        MelangeEmplacement emplacement = new MelangeEmplacement();
        emplacement.setEtat(false);
        model.addAttribute("emplacement", emplacement);
        return "formMelangeEmp";
    }

    ////////------------------Enregistrer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/addMelangeEmp", method = RequestMethod.POST)
    public String addMelangeEmp(@Valid MelangeEmplacement melangeEmp, BindingResult br, Model model) {
        MelangeEmplacement emplacement = melangeEmplacementRepository.findByEmplacement(melangeEmp.getEmplacement());
        if (emplacement == null) {
            model.addAttribute("emplacement", melangeEmp);
            if (br.hasErrors()) return "formMelangeEmp";
            melangeEmplacementRepository.save(melangeEmp);
            return "saveMelangeEmp";
        }
        return "formMelangeEmp";
    }

    ////////------------------Supprimer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteMelangeEmp", method = RequestMethod.POST)
    public String deleteMelangeEmp(Long id, int page, int size) {
        melangeEmplacementRepository.deleteById(id);
        return "redirect:/listEmplacements?page=" + page + "&size=" + size + "";
    }

}
