package ma.sacred.gestionstock.Web;

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
public class MachineController {
    @Autowired
    MachineRepository machineRepository;

    /////////////////----------------------Machine-----------------------////////////////
    ////////---------------Afficher les machine------------///////////
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/listMachines", method = GET)
    public String listMachine(Model model,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s,
                              @RequestParam(name = "keyword", defaultValue = "")String kw){
        Page<Machine> machine=machineRepository.findByReferenceContains(kw, PageRequest.of(p,s));
        model.addAttribute("result", machine.getTotalElements() );
        model.addAttribute("machines", machine.getContent());
        model.addAttribute("pages", new int[machine.getTotalPages()]);
        model.addAttribute("keyword", kw);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", p);
        return "listMachines";
    }

    /////////////////////////////////-----Ajouter machine----------////////////////////////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/formMachine", method = GET)
    public String formMachine(Model model){
        Machine machine=new Machine();
        machine.setEtat(false);
        model.addAttribute("machine", machine);
        return "formMachine";
    }

    ////////------------------Enregistrer machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/addMachine", method = RequestMethod.POST)
    public String addMachine(@Valid Machine machine, BindingResult br, Model model) {
        Machine machine1 = machineRepository.findByReferenceAndNom(machine.getReference(), machine.getNom());
        if (machine1 == null) {
            model.addAttribute("machine", machine);
            if (br.hasErrors()) return "formMelangeEmp";
            machineRepository.save(machine);
            return "saveMachine";
        }
        return "formMachine";
    }
    ////////------------------Modifier machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/editMachine", method = RequestMethod.GET)
    public String editMachine(Model model, Long id,
                              @RequestParam(name = "page", defaultValue = "0") int p,
                              @RequestParam(name = "size", defaultValue = "5") int s) {
        Machine machine=machineRepository.findById(id).get();
        model.addAttribute("machine", machine);
        return "formMachine";
    }

    ////////------------------Supprimer machine------------////////////
    @Secured(value = {"ROLE_ADMIN"})
    @RequestMapping(value = "/deleteMachine", method = RequestMethod.POST)
    public String deleteMachine(Long id, int page, int size) {
        machineRepository.deleteById(id);
        return "redirect:/listMachines?page=" + page + "&size=" + size + "";
    }
}
