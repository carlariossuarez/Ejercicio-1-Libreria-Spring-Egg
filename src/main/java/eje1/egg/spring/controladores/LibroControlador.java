package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Autor;
import eje1.egg.spring.entidades.Editorial;
import eje1.egg.spring.entidades.Libro;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.AutorServicio;
import eje1.egg.spring.servicios.EditorialServicio;
import eje1.egg.spring.servicios.LibroServicio;
import java.util.List;
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
@RequestMapping("/libros")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping
    public ModelAndView mostrarLibros(HttpServletRequest request) throws Exception, ErrorServicio {

        ModelAndView mav = new ModelAndView("libros");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("libros", libroServicio.obtenerLibros());
        return mav;

    }

    @GetMapping("/crear")
    public ModelAndView crearLibros() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("libros-formulario");
            mav.addObject("libro", new Libro());
            mav.addObject("autores", autorServicio.obtenerAutor());
            mav.addObject("editoriales", editorialServicio.obtenerEditorial());
            mav.addObject("title", "Crear Libro");
            mav.addObject("action", "guardar");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarLibros(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("libros-formulario");
            mav.addObject("libro", libroServicio.buscarPorId(id));
            mav.addObject("autores", autorServicio.obtenerAutor());
            mav.addObject("editoriales", editorialServicio.obtenerEditorial());
            mav.addObject("title", "Editar Libro");
            mav.addObject("action", "modificar");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/guardar")
    public RedirectView guardarLibros(@RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio,
            @RequestParam Integer ejemplares, @RequestParam ("editorial") String idEditorial, @RequestParam ("autor") String idAutor, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            libroServicio.crearLibro(isbn, titulo, anio, ejemplares, idEditorial, idAutor);
            attributes.addFlashAttribute("exito-name", "El libro ha sido creado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/libros");

    }

    @PostMapping("/modificar")
    public RedirectView modificarLibros(@RequestParam String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio,
            @RequestParam Integer ejemplares, @RequestParam ("editorial") String idEditorial, @RequestParam ("autor") String idAutor, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            libroServicio.modificarLibro(id, isbn, titulo, anio, ejemplares, idEditorial, idAutor);
            attributes.addFlashAttribute("exito-name", "El libro ha sido modificado exitosamente");
            
       
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/libros");

    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminarLibro(@PathVariable String id, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            libroServicio.bajaLibro(id);
            Libro libro = libroServicio.buscarPorId(id);
           attributes.addFlashAttribute("exito-name", "El libro ha sido "+((libro.getAlta())? "habilitado" : "deshabilitado")+"  exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/libros");

    }

}
