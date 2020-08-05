package ma.sacred.gestionstock.Web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @RequestMapping(value="/getLoggedUser")
    public Map<String ,Object> getLoggedUser(HttpServletRequest httpServletRequest){
        HttpSession httpSession=httpServletRequest.getSession();
        SecurityContext securityContext= (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        String username=securityContext.getAuthentication().getName();
        List<String> roles=new ArrayList<>();
        for (GrantedAuthority ga:securityContext.getAuthentication().getAuthorities()) {
            roles.add(ga.getAuthority());
        }
        Map<String, Object> params=new HashMap<>();
        params.put("username", username);
        params.put("roles", roles);
        return params;
    }

}
