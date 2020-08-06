package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.RoleRepository;
import ma.sacred.gestionstock.Dao.UserRepository;
import ma.sacred.gestionstock.Entities.MelangeReference;
import ma.sacred.gestionstock.Entities.Role;
import ma.sacred.gestionstock.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@Secured(value = {"ROLE_ADMIN"})
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    /////////////////----------------------Utilisateur-----------------------////////////////
    ////////---------------Afficher utilisateur------------///////////
    @RequestMapping(value = "listUsers", method = RequestMethod.GET)
    public String listUSers(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s,
                                 @RequestParam(name = "keyword", defaultValue = "") String kw) {
        Page<User> users=userRepository.findByUsernameContains(kw, PageRequest.of(p,s));
        model.addAttribute("users", users.getContent());
        model.addAttribute("pages", new int[users.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("size", s);
        model.addAttribute("keyword", kw);
        return "listUsers";
    }
    ////////---------------Ajouter utilisateur------------///////////
    @RequestMapping(value="formUser", method = RequestMethod.GET)
    public String formUser(Model model) {
        User user=new User();
        Collection<Role> roles=new ArrayList<Role>();
        user.setRoles(roles);
        model.addAttribute("user",user);
        List<Role> r =roleRepository.findAll();
        model.addAttribute("roles", r);
        return "formUser";
    }


    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult br, Model model) {
        user.setActive(true);
        model.addAttribute("user", user);
        if (br.hasErrors()) return "formUser";
        userRepository.save(user);
        return "redirect:/UserRole?username="+user.getUsername();
    }

    @RequestMapping(value = "UserRole", method = RequestMethod.GET)
    public String UserRole(Model model,
                           @RequestParam(name = "username")String username){
        User user=userRepository.findByUsername(username);
        List<Role> roles=roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "addUserRole";
    }
    @RequestMapping(value = "/addUserRole", method = RequestMethod.POST)
    public String addUserRole(Model model, String role, String username) {
        User user=userRepository.findByUsername(username);
        Role r=roleRepository.findByRole(role);
        user.getRoles().add(r);
        userRepository.save(user);
        model.addAttribute("user", user);
        model.addAttribute("role", r);
        return "saveUser";
    }

    ////////---------------Modifier utilisateur------------///////////
    @RequestMapping(value = "/editUser", method = RequestMethod.GET)
    public String editMelangeRef(Model model, String username) {
        User user=userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "formUser";
    }

    ////////---------------Supprimer utilisateur------------///////////
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(String username, int page, int size) {
        userRepository.deleteById(username);
        return "redirect:/listUsers?page=" + page + "&size=" + size + "";
    }
}
