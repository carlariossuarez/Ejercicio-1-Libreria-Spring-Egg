
package eje1.egg.spring.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Principal {
    
    @GetMapping
    public ModelAndView inicio(){
        return new ModelAndView ("index");
    }
}
