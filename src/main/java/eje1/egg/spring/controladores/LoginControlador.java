
package eje1.egg.spring.controladores;

import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.RolServicio;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;


@Controller
@RequestMapping("/login")
public class LoginControlador { 
    @Autowired
    private RolServicio rolServicio;
    
    
    @GetMapping
    public ModelAndView mostrarLogin(HttpServletRequest request, @RequestParam(required = false) String error, 
            @RequestParam(required = false) String logout, Principal principal) throws Exception, ErrorServicio {

        ModelAndView mav = new ModelAndView("login");
        
        if (error != null) { 
            mav.addObject("errorLogin", "Correo o contraseña inválida");
        }

        if (logout != null) {
            mav.addObject("logout", "Ha salido correctamente de la plataforma");
        }


        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }

        mav.addObject("roles", rolServicio.buscarTodos());
        return mav;

    }

}
