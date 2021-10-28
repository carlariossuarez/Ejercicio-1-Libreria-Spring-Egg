package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Autor;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping
    public ModelAndView mostrarAutores() throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("autores");
            mav.addObject("autores", autorServicio.obtenerAutor());
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

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
    public RedirectView guardarAutores(@RequestParam String nombre)throws Exception, ErrorServicio {
        try {
            autorServicio.crearAutor(nombre);
        return new RedirectView("/autores");
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
    }

    @PostMapping("/modificar")
    public RedirectView modificarAutores(@RequestParam String id, @RequestParam String nombre)throws Exception, ErrorServicio  {
        try {
            autorServicio.modificarAutor(id, nombre);
        return new RedirectView("/autores");
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarAutor(@PathVariable String id)throws Exception, ErrorServicio {
        try {
            autorServicio.bajarAutor(id);
        return new RedirectView("/autores");
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
    }

}
