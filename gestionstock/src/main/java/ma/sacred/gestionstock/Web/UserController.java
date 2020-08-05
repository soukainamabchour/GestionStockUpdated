package ma.sacred.gestionstock.Web;

import ma.sacred.gestionstock.Dao.RoleRepository;
import ma.sacred.gestionstock.Dao.UserRepository;
import ma.sacred.gestionstock.Entities.Role;
import ma.sacred.gestionstock.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    ////////---------------Ajouter utilisateur------------///////////
    @RequestMapping(value="formUser", method = RequestMethod.GET)
    public String formUser(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles =roleRepository.findAll();
        model.addAttribute("roles", roles);
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
    public String addUserRole(String role, String username) {
        User user=userRepository.findByUsername(username);
        Role r=roleRepository.findByRole(role);
        user.getRoles().add(r);
        userRepository.save(user);
        return "homepage";
    }

}
