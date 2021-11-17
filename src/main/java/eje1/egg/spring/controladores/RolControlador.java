
package eje1.egg.spring.controladores;

import eje1.egg.spring.servicios.RolServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/rol")
public class RolControlador {
    
    @Autowired
    RolServicio rolServicio;
    

    
    @PostMapping("/guardar")
    public RedirectView guardar() {
        rolServicio.crear("ADMIN");
         rolServicio.crear("USUARIO");
        return new RedirectView("/login");
    }
}
