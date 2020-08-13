package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.FinRepository;
import ma.sacred.gestionstock.Dao.MachineRepository;
import ma.sacred.gestionstock.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Mac;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class FinController {
    @Autowired
    FinRepository finRepository;

    /////////////////----------------------Machine-----------------------////////////////
    ////////---------------Afficher les machine------------///////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listFins", method = GET)
    public String listFins(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "keyword", defaultValue = "")String kw){
        Page<Fin> fins=finRepository.findByPlaceContains(kw, PageRequest.of(p,s));
        model.addAttribute("result", fins.getTotalElements() );
        model.addAttribute("fins", fins.getContent());
        model.addAttribute("pages", new int[fins.getTotalPages()]);
        model.addAttribute("keyword", kw);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        return "listFins";
    }

    /////////////////////////////////-----Ajouter machine----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/formFin", method = GET)
    public String formFin(Model model){
        Fin fin=new Fin();
        model.addAttribute("fin", fin);
        return "formFin";
    }

    ////////------------------Enregistrer machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/addFin", method = RequestMethod.POST)
    public String addFin(@Valid Fin fin, BindingResult br, Model model) {
        Fin fin1 = finRepository.findByPlace(fin.getPlace());
        if (fin1 == null) {
            model.addAttribute("fin", fin);
            if (br.hasErrors()) return "formFin";
            finRepository.save(fin);
            return "saveFin";
        }
        return "formFin";
    }
    ////////------------------Modifier machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editFin", method = RequestMethod.GET)
    public String editFin(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s) {
        Fin fin=finRepository.findById(id).get();
        model.addAttribute("fin", fin);
        return "formFin";
    }

    ////////------------------Supprimer machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteFin", method = RequestMethod.POST)
    public String deleteFin(Long id, int page, int size) {
        finRepository.deleteById(id);
        return "redirect:/listFins?page=" + page + "&size=" + size + "";
    }
}
