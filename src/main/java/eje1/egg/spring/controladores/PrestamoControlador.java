package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Prestamo;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.ClienteServicio;
import eje1.egg.spring.servicios.LibroServicio;
import eje1.egg.spring.servicios.PrestamoServicio;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/prestamos")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private LibroServicio libroServicio;

    @GetMapping
    public ModelAndView mostrarPrestamos(HttpServletRequest request) throws Exception, ErrorServicio {
        ModelAndView mav = new ModelAndView("prestamos");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("prestamos", prestamoServicio.obtenerPrestamos());
        return mav;
    }

    @GetMapping("/crear-prestamos")
    public ModelAndView crearPrestamos() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("prestamos-formulario");
            mav.addObject("prestamo", new Prestamo());
            mav.addObject("libros", libroServicio.obtenerLibros());
            mav.addObject("clientes", clienteServicio.obtenerClientes());
            mav.addObject("title", "Crear Prestamo");
            mav.addObject("action", "guardar-prestamos");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @GetMapping("/editar-prestamos/{id}")
    public ModelAndView editarPrestamos(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("prestamos-formulario");
            mav.addObject("prestamo", prestamoServicio.buscarPorId(id));
            mav.addObject("libros", libroServicio.obtenerLibros());
            mav.addObject("clientes", clienteServicio.obtenerClientes());
            mav.addObject("title", "Editar Prestamo");
            mav.addObject("action", "modificar-prestamos");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/guardar-prestamos")
    public RedirectView guardarPrestamos(@RequestParam LocalDate fechaDevolucion, @RequestParam("libro") String idLibro,
            @RequestParam("cliente") String idCliente, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            prestamoServicio.crearPrestamo(fechaDevolucion, idLibro, idCliente);
            attributes.addFlashAttribute("exito-name", "El prestamo ha sido creado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/prestamos");
    }

    @PostMapping("/modificar-prestamos")
    public RedirectView modificarPrestamos(@RequestParam String id, @RequestParam LocalDate fechaDevolucion, @RequestParam("libro") String idLibro,
            @RequestParam("cliente") String idCliente, RedirectAttributes attributes) throws Exception, ErrorServicio {

        try {
            prestamoServicio.modificarPrestamo(id, fechaDevolucion, idLibro, idCliente);
            attributes.addFlashAttribute("exito-name", "El prestamo ha sido modificado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/prestamos");
    }

    @PostMapping("/eliminar-prestamos/{id}")
    public RedirectView eliminarPrestamos(@PathVariable String id, RedirectAttributes attributes) throws Exception, ErrorServicio {

        try {
            prestamoServicio.bajaPrestamo(id);
            Prestamo prestamo = prestamoServicio.buscarPorId(id);
            attributes.addFlashAttribute("exito-name", "El prestamo ha sido " + ((prestamo.getAlta()) ? "habilitado" : "deshabilitado") + "  exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/prestamos");
    }

}
