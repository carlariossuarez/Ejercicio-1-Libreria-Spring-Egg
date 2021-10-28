package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Editorial;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.EditorialServicio;
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
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping
    public ModelAndView mostrarEditoriales() throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("editoriales");
            mav.addObject("editoriales", editorialServicio.obtenerEditorial());
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @GetMapping("/crear")
    public ModelAndView crearEditoriales() {
        ModelAndView mav = new ModelAndView("editoriales-formulario");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarAutores(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("editoriales-formulario");
            mav.addObject("editorial", editorialServicio.buscarPorId(id));
            mav.addObject("title", "Editar Editorial");
            mav.addObject("action", "modificar");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/guardar")
    public RedirectView guardarEditoriales(@RequestParam String nombre) throws Exception, ErrorServicio {
        try {
            editorialServicio.crearEditorial(nombre);
            return new RedirectView("/editoriales");
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/modificar")
    public RedirectView modificarEditoriales(@RequestParam String id, @RequestParam String nombre) throws Exception, ErrorServicio {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            return new RedirectView("/editoriales");
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarEditorial(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            editorialServicio.bajarEditorial(id);
            return new RedirectView("/editoriales");
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }
}
