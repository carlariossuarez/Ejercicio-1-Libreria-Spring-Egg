package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Autor;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.AutorServicio;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping
    public ModelAndView mostrarAutores(HttpServletRequest request) throws Exception, ErrorServicio {

        ModelAndView mav = new ModelAndView("autores");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }

        mav.addObject("autores", autorServicio.obtenerAutor());
        return mav;

    }

    @GetMapping("/crear")
    public ModelAndView crearAutores() {
        ModelAndView mav = new ModelAndView("autores-formulario");
        mav.addObject("autor", new Autor());
        mav.addObject("title", "Crear Autor");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarAutores(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("autores-formulario");
            mav.addObject("autor", autorServicio.buscarPorId(id));
            mav.addObject("title", "Editar Autor");
            mav.addObject("action", "modificar");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/guardar")
    public RedirectView guardarAutores(@RequestParam String nombre, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            autorServicio.crearAutor(nombre);
            attributes.addFlashAttribute("exito-name", "El autor ha sido creado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/autores");
    }

    @PostMapping("/modificar")
    public RedirectView modificarAutores(@RequestParam String id, @RequestParam String nombre, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            autorServicio.modificarAutor(id, nombre);
            attributes.addFlashAttribute("exito-name", "El autor ha sido modificado exitosamente");
        
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/autores");

    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarAutor(@PathVariable String id, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            autorServicio.bajarAutor(id);
            Autor autor = autorServicio.buscarPorId(id);
            attributes.addFlashAttribute("exito-name", "El autor ha sido " +((autor.getAlta())? "habilitado" : "deshabilitado")+ " exitosamente");
            
       
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/autores");

    }

}
