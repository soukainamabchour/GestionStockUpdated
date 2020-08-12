package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.*;
import ma.sacred.gestionstock.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
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
public class ComposantController {

    @Autowired
    ComposantReferenceRepository composantReferenceRepository;

    @Autowired
    ComposantRepository composantRepository;

    @Autowired
    ComposantEmplacementRepository composantEmplacementRepository;


    @RequestMapping(value = "/composants", method = RequestMethod.GET)
    public String melanges() {
        return "Composants";
    }
    /////////////////----------------------Reference Composant-----------------------////////////////
    ////////---------------Lister références------------///////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(path = "/composantRef")
    public String listComposantRef(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<ComposantReference> composantRef = composantReferenceRepository.findByReferenceContains(kw, PageRequest.of(p, s));
        model.addAttribute("composantRef", composantRef.getContent());
        model.addAttribute("pages", new int[composantRef.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", s);
        model.addAttribute("keyword", kw);
        return "listComposantRef";
    }

    ////////------------------Ajouter référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formComposantRef", method = RequestMethod.GET)
    public String formComposantRef(Model model) {
        model.addAttribute("composantRef", new ComposantReference());
        return "formComposantRef";
    }


    ////////------------------Enregistrer référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/addComposantRef")
    public String addComposantRef(@Valid  ComposantReference composantRef, BindingResult bindingResult, Model model) {
        ComposantReference reference=composantReferenceRepository.findByReference(composantRef.getReference());
        if(reference==null) {
            model.addAttribute("composantRef", composantRef);
            if (bindingResult.hasErrors()) return "formComposantRef";
            composantReferenceRepository.save(composantRef);
            return "saveComposantRef";
        }
        return "formComposantRef";
    }

    ////////------------------Modifier référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editComposantRef", method = RequestMethod.GET)
    public String editComposantRef(Model model, Long id) {
        ComposantReference composantRef = composantReferenceRepository.findById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("composantRef", composantRef);
        return "formComposantRef";
    }

    ////////------------------Supprimer référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteComposantRef", method = RequestMethod.POST)
    public String deleteComposantRef(Long id, int page, int size) {
        composantReferenceRepository.deleteById(id);
        return "redirect:/composantRef?page=" + page + "&size=" + size + "";
    }

    /////////////////----------------------Composant-----------------------////////////////
    /////////////////////////////////-----Lister tous les composants----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listerComposants", method = GET)
    public String listerComposants (Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "")String kw
    ){
        Page<Composant> composants=composantRepository.findAllByNomContainsOrderByJoursAsc(kw, PageRequest.of(p,s));
        model.addAttribute("result", composants.getTotalElements() );
        model.addAttribute("listComposant", composants.getContent());
        model.addAttribute("pages", new int[composants.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("keyword", kw);
        return "listerComposants";
    }

    /////////////////////////////////-----Retirer----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/useComposants", method = RequestMethod.POST)
    public String useMelanges(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "id")Long id){
        Composant composant=composantRepository.findById(id).get();
        Long emp=composant.getEmplacement().getId();
        ComposantEmplacement old_emp=composantEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        composant.setEmplacement(null);
        composant.setDateUtilisation(LocalDateTime.now());
        composantRepository.save(composant);
        return "redirect:/listerComposants?page=" + p + "&size=" + s +"";
    }

    /////////////////////////////////-----Ajouter----------////////////////////////////////

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formComposants", method = RequestMethod.GET)
    public String formComposants(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int p,
                               @RequestParam(name = "size", defaultValue = "5") int s){
        List<ComposantReference> references=composantReferenceRepository.findAll();
        List<ComposantEmplacement> emplacements=composantEmplacementRepository.findByEtatIsFalse();
        Composant composant=new Composant();
        model.addAttribute("composant", composant);
        model.addAttribute("emplacement",emplacements);
        model.addAttribute("references", references);
        return "formComposants";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addComposants", method = RequestMethod.POST)
    public String addComposants(@Valid Composant composant, BindingResult br, Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s) {
        Composant composant1=composantRepository.findByNom(composant.getNom());
        if(composant1==null) {
            if (br.hasErrors()) return "formComposants";
            composant.getEmplacement().setEtat(true);
            composant.setJours(90 - ChronoUnit.DAYS.between(composant.getDateFabrication(), LocalDate.now()));
            model.addAttribute("composant", composant);
            composantRepository.save(composant);
            return "saveComposants";
        }
        return "formComposants";
    }

    /////////////////////////////////-----Lister  composants par ref----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listComposant", method = GET)
    public String listComposant(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long ref_id,
                              @RequestParam(name = "ref")String ref,
                              @RequestParam(name = "keyword", defaultValue ="") String kw
    ) {
        Page<Composant> composant = composantRepository.findByReference_IdAndNomContainsOrderByJoursAsc(ref_id,kw, PageRequest.of(p, s));
        model.addAttribute("result",composant.getTotalElements() );
        model.addAttribute("listComposant", composant.getContent());
        model.addAttribute("pages", new int[composant.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("ref", ref);
        model.addAttribute("keyword", kw);
        return "listComposant";
    }

    ////////------------------Ajouter composant------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formComposant", method = RequestMethod.GET)
    public String formcomposant(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long id,
                              @RequestParam(name = "ref")String ref){
        List<ComposantEmplacement> emplacements=composantEmplacementRepository.findByEtatIsFalse();
        Composant composant=new Composant();
        ComposantReference reference=composantReferenceRepository.findById(id).get();
        composant.setReference(reference);
        model.addAttribute("composant", composant);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        model.addAttribute("emplacement",emplacements);
        return "formComposant";
    }

    ////////------------------Enregistrer composant------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addComposant", method = RequestMethod.POST)
    public String addComposant(@Valid Composant composant, BindingResult br, Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name="ref_id")Long id,
                             @RequestParam(name = "ref")String ref) {
        Composant composant1=composantRepository.findByNom(composant.getNom());
        if(composant1==null) {
            if (br.hasErrors()) return "formComposant";
            ComposantReference reference = composantReferenceRepository.findById(id).get();
            composant.getEmplacement().setEtat(true);
            composant.setJours(90 - ChronoUnit.DAYS.between(composant.getDateFabrication(), LocalDate.now()));
            model.addAttribute("composant", composant);
            model.addAttribute("ref_id", id);
            model.addAttribute("ref", ref);
            composant.setReference(reference);
            composantRepository.save(composant);
            return "saveComposant";
        }
        return "formComposant";
    }

    ////////------------------Utiliser mélange------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/useComposant", method = RequestMethod.POST)
    public String useComposant(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name = "ref_id") Long ref_id,
                             @RequestParam(name = "ref")String ref,
                             @RequestParam(name = "id")Long id,
                             @RequestParam(name = "keyword", defaultValue ="") String kw){
        Composant composant=composantRepository.findById(id).get();
        Long emp=composant.getEmplacement().getId();
        ComposantEmplacement old_emp=composantEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        composant.setEmplacement(null);
        composant.setDateUtilisation(LocalDateTime.now());
        composantRepository.save(composant);
        return "redirect:/listComposant?ref_id="+ref_id+"&ref="+ref+"&page=" + p + "&size=" + s + "&keyword="+kw+"";
    }

    ////////------------------Modifier composant------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editComposant", method = RequestMethod.GET)
    public String editComposant(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long ref_id,
                              @RequestParam(name = "ref")String ref){
        Composant composant= composantRepository.findByIdAndReference_Id(id, ref_id);
        Long emp=composant.getEmplacement().getId();
        ComposantEmplacement old_emp=composantEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        composantEmplacementRepository.save(old_emp);
        List<ComposantEmplacement> emplacements=composantEmplacementRepository.findByEtatIsFalse();
        model.addAttribute("composant", composant);
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("emplacement",emplacements);
        return "formComposant";
    }

    ////////------------------Supprimer composant------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteComposant", method = RequestMethod.POST)
    public String deleteComposant(Model model, Long id,
                                @RequestParam(name = "ref_id")Long ref_id,
                                @RequestParam(name = "ref")String ref,
                                @RequestParam(name = "keyword", defaultValue = "")String keyword,
                                int page, int size) {
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        composantRepository.deleteById(id);
        return "redirect:/listComposant?ref_id="+ref_id+"&ref="+ref+"&page=" + page + "&size=" + size + "&keyword="+keyword+"";
    }
    /////////////////---------------------- Emplacement Composant-----------------------////////////////

    /////////////////////////////////-----Lister les emplacements----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listEmplacementsC", method = GET)
    public String listEmplacementsC(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int p,
                                   @RequestParam(name = "size", defaultValue = "5") int s,
                                   @RequestParam(name = "keyword", defaultValue = "")String kw){
        Page<ComposantEmplacement> emplacements=composantEmplacementRepository.findByEmplacementContainsOrderByEmplacement(kw, PageRequest.of(p,s));
        model.addAttribute("result", emplacements.getTotalElements() );
        model.addAttribute("emplacements", emplacements.getContent());
        model.addAttribute("pages", new int[emplacements.getTotalPages()]);
        model.addAttribute("keyword", kw);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        return "listEmplacementsC";
    }

    /////////////////////////////////-----Ajouter emplacement----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/formComposantEmp", method = GET)
    public String formComposantEmp(Model model){
        ComposantEmplacement emplacement=new ComposantEmplacement();
        emplacement.setEtat(false);
        model.addAttribute("emplacement", emplacement);
        return "formComposantEmp";
    }

    ////////------------------Enregistrer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/addComposantEmp", method = RequestMethod.POST)
    public String addComposantEmp(@Valid ComposantEmplacement composantEmp, BindingResult br, Model model) {
        ComposantEmplacement emplacement=composantEmplacementRepository.findByEmplacement(composantEmp.getEmplacement());
        if(emplacement==null) {
            model.addAttribute("emplacement", composantEmp);
            if (br.hasErrors()) return "formComposantEmp";
            composantEmplacementRepository.save(composantEmp);
            return "saveComposantEmp";
        }
        return "formComposantEmp";
    }
    ////////------------------Supprimer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteComposantEmp", method = RequestMethod.POST)
    public String deleteComposantEmp(Long id, int page, int size) {
        composantEmplacementRepository.deleteById(id);
        return "redirect:/listEmplacementsC?page=" + page + "&size=" + size + "";
    }

}
