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
public class ProduitChimiqueController {

    @Autowired
    PCReferenceRepository pcReferenceRepository;

    @Autowired
    ProduitChimiqueRepository pcRepository;

    @Autowired
    PCEmplacementRepository pcEmplacementRepository;

    @RequestMapping(value = "/produitsChimiques", method = RequestMethod.GET)
    public String produitsChimiques() {
        return "produitsChimiques";
    }
    /////////////////----------------------Reference pc-----------------------////////////////
    ////////---------------Lister références------------///////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(path = "/pcRef")
    public String listRefPC(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<PCReference> references = pcReferenceRepository.findByReferenceContains(kw, PageRequest.of(p, s));
        model.addAttribute("references", references.getContent());
        model.addAttribute("pages", new int[references.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", s);
        model.addAttribute("keyword", kw);
        return "listPCRef";
    }

    ////////------------------Ajouter référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formPCRef", method = RequestMethod.GET)
    public String formMelangeRef(Model model) {
        model.addAttribute("pcRef", new PCReference());
        return "formPCRef";
    }


    ////////------------------Enregistrer référence------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = "/addPCRef")
    public String addPCRef(@Valid  PCReference pcReference, BindingResult bindingResult, Model model) {
        model.addAttribute("pcRef", pcReference);
        if (bindingResult.hasErrors()) return "formPCRef";
        pcReferenceRepository.save(pcReference);
        return "savePCRef";
    }

    ////////------------------Modifier référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editPCRef", method = RequestMethod.GET)
    public String editPCRef(Model model, Long id) {
        PCReference pcReference= pcReferenceRepository.findById(id).get();
        model.addAttribute("id", id);
        model.addAttribute("pcRef", pcReference);
        return "formPCRef";
    }

    ////////------------------Supprimer référence------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deletePCRef", method = RequestMethod.POST)
    public String deletePCRef(Long id, int page, int size) {
        pcReferenceRepository.deleteById(id);
        return "redirect:/pcRef?page=" + page + "&size=" + size + "";
    }

    /////////////////---------------------- Melange-----------------------////////////////
    /////////////////////////////////-----Lister tous les produits chimiques----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listerPCs", method = GET)
    public String listerPCs(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "")String kw
    ){
        Page<ProduitChimique> produitChimiques=pcRepository.findAllByNomContainsOrderByJoursAsc(kw, PageRequest.of(p,s));
        model.addAttribute("result", produitChimiques.getTotalElements() );
        model.addAttribute("listPC", produitChimiques.getContent());
        model.addAttribute("pages", new int[produitChimiques.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("keyword", kw);
        return "listerPCs";
    }

    /////////////////////////////////-----Retirer----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/usePCs", method = RequestMethod.POST)
    public String usePCs(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "id")Long id){
        ProduitChimique produitChimique=pcRepository.findById(id).get();
        Long emp=produitChimique.getEmplacement().getId();
        PCEmplacement old_emp=pcEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        produitChimique.setEmplacement(null);
        produitChimique.setDateUtilisation(LocalDateTime.now());
        pcRepository.save(produitChimique);
        return "redirect:/listerPCs?page=" + p + "&size=" + s +"";
    }

    /////////////////////////////////-----Ajouter----------////////////////////////////////

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formPCs", method = RequestMethod.GET)
    public String formPCs(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int p,
                               @RequestParam(name = "size", defaultValue = "5") int s){
        List<PCReference> references=pcReferenceRepository.findAll();
        List<PCEmplacement> emplacements=pcEmplacementRepository.findByEtatIsFalse();
        ProduitChimique produitChimique=new ProduitChimique();
        model.addAttribute("pc", produitChimique);
        model.addAttribute("emplacement",emplacements);
        model.addAttribute("references", references);
        return "formPCs";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addPCs", method = RequestMethod.POST)
    public String addPCs(@Valid ProduitChimique produitChimique, BindingResult br, Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s) {
        if(br.hasErrors()) return "formPCs";
        produitChimique.getEmplacement().setEtat(true);
        produitChimique.setJours(90-ChronoUnit.DAYS.between(produitChimique.getDateFabrication(), LocalDate.now()));
        model.addAttribute("pc", produitChimique);
        pcRepository.save(produitChimique);
        return "savePCs";
    }

    /////////////////////////////////-----Lister  pcs par ref----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listPC", method = GET)
    public String listPC(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id") Long ref_id,
                              @RequestParam(name = "ref")String ref,
                              @RequestParam(name = "keyword", defaultValue ="") String kw
    ) {
        Page<ProduitChimique> produitChimiques = pcRepository.findByReference_IdAndNomContainsOrderByJoursAsc(ref_id,kw, PageRequest.of(p, s));
        model.addAttribute("result", produitChimiques.getTotalElements() );
        model.addAttribute("listPC", produitChimiques.getContent());
        model.addAttribute("pages", new int[produitChimiques.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("ref", ref);
        model.addAttribute("keyword", kw);
        return "listPC";
    }

    ////////------------------Ajouter produit chimique------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/formPC", method = RequestMethod.GET)
    public String formPC(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long id,
                              @RequestParam(name = "ref")String ref){
        List<PCEmplacement> emplacements=pcEmplacementRepository.findByEtatIsFalse();
        ProduitChimique produitChimique=new ProduitChimique();
        PCReference reference=pcReferenceRepository.findById(id).get();
        produitChimique.setReference(reference);
        model.addAttribute("pc", produitChimique);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        model.addAttribute("emplacement",emplacements);
        return "formPC";
    }

    ////////------------------Enregistrer mélange------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/addPC", method = RequestMethod.POST)
    public String addPC(@Valid ProduitChimique produitChimique, BindingResult br, Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name="ref_id")Long id,
                             @RequestParam(name = "ref")String ref) {
        if(br.hasErrors()) return "formPC";
        PCReference reference = pcReferenceRepository.findById(id).get();
        produitChimique.getEmplacement().setEtat(true);
        produitChimique.setJours(90-ChronoUnit.DAYS.between(produitChimique.getDateFabrication(), LocalDate.now()));
        model.addAttribute("pc", produitChimique);
        model.addAttribute("ref_id", id);
        model.addAttribute("ref", ref);
        produitChimique.setReference(reference);
        pcRepository.save(produitChimique);
        return "savePC";
    }

    ////////------------------Utiliser produit chimique------------////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/usePC", method = RequestMethod.POST)
    public String usePC(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "5") int s,
                             @RequestParam(name = "ref_id") Long ref_id,
                             @RequestParam(name = "ref")String ref,
                             @RequestParam(name = "id")Long id,
                             @RequestParam(name = "keyword", defaultValue ="") String kw){
        ProduitChimique produitChimique=pcRepository.findById(id).get();
        Long emp=produitChimique.getEmplacement().getId();
        PCEmplacement old_emp=pcEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        produitChimique.setEmplacement(null);
        produitChimique.setDateUtilisation(LocalDateTime.now());
        pcRepository.save(produitChimique);
        return "redirect:/listPC?ref_id="+ref_id+"&ref="+ref+"&page=" + p + "&size=" + s + "&keyword="+kw+"";
    }

    ////////------------------Modifier Produit chimique------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editPC", method = RequestMethod.GET)
    public String editPC(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "ref_id")Long ref_id,
                              @RequestParam(name = "ref")String ref){
        ProduitChimique produitChimique= pcRepository.findByIdAndReference_Id(id, ref_id);
        Long emp=produitChimique.getEmplacement().getId();
        PCEmplacement old_emp=pcEmplacementRepository.findById(emp).get();
        old_emp.setEtat(false);
        pcEmplacementRepository.save(old_emp);
        List<PCEmplacement> emplacements=pcEmplacementRepository.findByEtatIsFalse();
        model.addAttribute("pc", produitChimique);
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        model.addAttribute("emplacement",emplacements);
        return "formPC";
    }

    ////////------------------Supprimer produit chimique------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deletePC", method = RequestMethod.POST)
    public String deletePC(Model model, Long id,
                                @RequestParam(name = "ref_id")Long ref_id,
                                @RequestParam(name = "ref")String ref,
                                @RequestParam(name = "keyword", defaultValue = "")String keyword,
                                int page, int size) {
        model.addAttribute("ref", ref);
        model.addAttribute("ref_id", ref_id);
        pcRepository.deleteById(id);
        return "redirect:/listPC?ref_id="+ref_id+"&ref="+ref+"&page=" + page + "&size=" + size + "&keyword="+keyword+"";
    }
    /////////////////---------------------- Emplacement produit chimique-----------------------////////////////

    /////////////////////////////////-----Lister les emplacements----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listEmplacementsPC", method = GET)
    public String listEmplacementsPC(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int p,
                                   @RequestParam(name = "size", defaultValue = "5") int s,
                                   @RequestParam(name = "keyword", defaultValue = "")String kw){
        Page<PCEmplacement> emplacements=pcEmplacementRepository.findByEmplacementContainsOrderByEmplacement(kw, PageRequest.of(p,s));
        model.addAttribute("result", emplacements.getTotalElements() );
        model.addAttribute("emplacements", emplacements.getContent());
        model.addAttribute("pages", new int[emplacements.getTotalPages()]);
        model.addAttribute("keyword", kw);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        return "listEmplacementsPC";
    }

    /////////////////////////////////-----Ajouter emplacement----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/formPCEmp", method = GET)
    public String formPCEmp(Model model){
        PCEmplacement emplacement=new PCEmplacement();
        emplacement.setEtat(false);
        model.addAttribute("emplacement", emplacement);
        return "formPCEmp";
    }

    ////////------------------Enregistrer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/addPCEmp", method = RequestMethod.POST)
    public String addPCEmp(@Valid PCEmplacement emplacement, BindingResult br, Model model) {
        model.addAttribute("emplacement", emplacement);
        if (br.hasErrors()) return "formMelangeEmp";
        pcEmplacementRepository.save(emplacement);
        return "savePCEmp";
    }
    ////////------------------Supprimer emplacement------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deletePCEmp", method = RequestMethod.POST)
    public String deletePCEmp(Long id, int page, int size) {
        pcEmplacementRepository.deleteById(id);
        return "redirect:/listEmplacementsPC?page=" + page + "&size=" + size + "";
    }

}

