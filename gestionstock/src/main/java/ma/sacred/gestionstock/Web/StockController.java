package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.MelangeEmplacementRepository;
import ma.sacred.gestionstock.Dao.MelangeReferenceRepository;
import ma.sacred.gestionstock.Dao.MelangeRepository;
import ma.sacred.gestionstock.Entities.Melange;
import ma.sacred.gestionstock.Entities.MelangeEmplacement;
import ma.sacred.gestionstock.Entities.MelangeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class StockController {

    @Autowired
    MelangeReferenceRepository melangeReferenceRepository;

    @Autowired
    MelangeRepository melangeRepository;

    @Autowired
    MelangeEmplacementRepository melangeEmplacementRepository;


    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String homepage() {
        return "homepage";
    }

    /////////////////----------------------Reference Melange-----------------------////////////////
    ////////---------------Lister références------------///////////
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
    @RequestMapping(value = "/formMelangeRef", method = RequestMethod.GET)
    public String formMelangeRef(Model model) {
        model.addAttribute("melangeRef", new MelangeReference());
        return "formMelangeRef";
    }


    ////////------------------Enregistrer référence------------////////////
    @RequestMapping(value = "/addMelangeRef", method = RequestMethod.POST)
    public String addMelangeRef(@Valid MelangeReference melangeRef, BindingResult br, Model model) {
        model.addAttribute("melangeRef", melangeRef);
        if (br.hasErrors()) return "formMelangeRef";
        melangeReferenceRepository.save(melangeRef);
        return "saveMelangeRef";
    }

     ////////------------------Modifier référence------------////////////
    @RequestMapping(value = "/editMelangeRef", method = RequestMethod.GET)
    public String editMelangeRef(Model model, Long id) {
        MelangeReference melangeRef = melangeReferenceRepository.findById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("melangeRef", melangeRef);
        return "formMelangeRef";
    }

    ////////------------------Supprimer référence------------////////////
    @RequestMapping(value = "/deleteMelangeRef", method = RequestMethod.POST)
    public String deleteMelangeRef(Long id, int page, int size) {
        melangeReferenceRepository.deleteById(id);
        return "redirect:/melangeRef?page=" + page + "&size=" + size + "";
    }

    /////////////////---------------------- Melange-----------------------////////////////
    /////////////////////////////////-----Lister mélanges----------////////////////////////////////
    @RequestMapping(value = "/melange", method = GET)
    public String listMelange(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long ref_id,
                              @RequestParam(name = "ref")String ref,
                              @RequestParam(name = "keyword", defaultValue ="") String kw
    ) {
        Page<Melange> melange = melangeRepository.findByReference_IdAndLotContainsOrderByJoursAsc(ref_id,kw, PageRequest.of(p, s));
        melange.forEach(m->{
            m.setJours(90- ChronoUnit.DAYS.between(m.getDateFabrication(), LocalDate.now()));
        });
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

    ////////------------------Ajouter mélange------------////////////
    @RequestMapping(value = "/formMelange", method = RequestMethod.GET)
    public String formMelange(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long id,
                              @RequestParam(name = "ref")String ref){
        Page<MelangeEmplacement> emplacements=melangeEmplacementRepository.findByEtatIsFalse(PageRequest.of(p, s));
        Melange melange=new Melange();
        model.addAttribute("melange", melange);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        model.addAttribute("emplacement",emplacements);
        return "formMelange";
    }

    ////////------------------Enregistrer mélange------------////////////
    @RequestMapping(value = "/addMelange", method = RequestMethod.POST)
    public String addMelange(@Valid Melange melange, BindingResult br, Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name="ref_id")Long id,
                             @RequestParam(name = "ref")String ref) {
        MelangeReference reference = melangeReferenceRepository.findById(id).get();
        melange.getEmplacement().setEtat(true);
        model.addAttribute("melange", melange);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        melange.setReference(reference);
        melangeRepository.save(melange);
        return "saveMelange";
    }

    ////////------------------Utiliser mélange------------////////////
    @RequestMapping(value = "/useMelange", method = RequestMethod.POST)
    public String useMelange(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name = "ref_id") Long ref_id,
                             @RequestParam(name = "ref")String ref,
                             @RequestParam(name = "id")Long id,
                             @RequestParam(name = "keyword", defaultValue ="") String kw){
        Melange melange=melangeRepository.findById(id).get();
        melange.setDateUtilisation(LocalDateTime.now());
        melangeRepository.save(melange);
        return "listMelange";
    }

    ////////------------------Modifier mélange------------////////////
    @RequestMapping(value = "/editMelange", method = RequestMethod.GET)
    public String editMelange(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long ref_id,
                              @RequestParam(name = "ref")String ref,
                              @RequestParam(name = "emp")Long emp){
        MelangeEmplacement old_emp=melangeEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        melangeEmplacementRepository.save(old_emp);
        Page<MelangeEmplacement> emplacements=melangeEmplacementRepository.findByEtatIsFalse(PageRequest.of(p, s));
        Melange melange= melangeRepository.findByIdAndReference_Id(id, ref_id);
        model.addAttribute("melange", melange);
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("emplacement",emplacements);
        return "formMelange";
    }

    ////////------------------Supprimer mélange------------////////////
    @RequestMapping(value = "/deleteMelange", method = RequestMethod.POST)
    public String deleteMelange(Model model, Long id,
                                @RequestParam(name = "ref_id")Long ref_id,
                                @RequestParam(name = "ref")String ref,
                                @RequestParam(name = "keyword", defaultValue = "")String keyword,
                                int page, int size) {
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        melangeRepository.deleteById(id);
        return "redirect:/melange?ref_id="+ref_id+"&ref="+ref+"&page=" + page + "&size=" + size + "&keyword="+keyword+"";
    }

}
