package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Cliente;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.ClienteServicio;
import eje1.egg.spring.servicios.PrestamoServicio;
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
@RequestMapping("/clientes")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private PrestamoServicio prestamoServicio;
    

    @GetMapping
    public ModelAndView mostrarClientes(HttpServletRequest request) throws Exception, ErrorServicio {
        ModelAndView mav = new ModelAndView("clientes");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("clientes", clienteServicio.obtenerClientes());
        
        return mav;
    }
    
    @GetMapping("/mostrar-prestamos-clientes/{id}")
    public ModelAndView prestamosClientes(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("prestamosClientes");
        mav.addObject("clientePrestamo", prestamoServicio.prestamosClienteId(id));
        return mav;
        } catch (ErrorServicio ex) {
            throw new ErrorServicio("Error al obtener prestamos de clientes");
        }catch (Exception e){
            throw e;
        }
        
    }

    @GetMapping("/crear-clientes")
    public ModelAndView crearClientes() {
        ModelAndView mav = new ModelAndView("clientes-formulario");
        mav.addObject("cliente", new Cliente());
        mav.addObject("title", "Crear Cliente");
        mav.addObject("action", "guardar-clientes");
        return mav;
    }

    @GetMapping("/editar-clientes/{id}")
    public ModelAndView editarClientes(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("clientes-formulario");
            mav.addObject("cliente", clienteServicio.buscarPorId(id));
            mav.addObject("title", "Editar Cliente");
            mav.addObject("action", "modificar-clientes");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/guardar-clientes")
    public RedirectView guardarClientes(@RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            clienteServicio.crearCliente(documento, nombre, apellido, telefono);
            attributes.addFlashAttribute("exito-name", "El cliente ha sido creado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/clientes");

    }

    @PostMapping("/modificar-clientes")
    public RedirectView modificarClientes(@RequestParam String id, @RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            clienteServicio.modificarCliente(id, documento, nombre, apellido, telefono);
            attributes.addFlashAttribute("exito-name", "El cliente ha sido modificado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/clientes");
    }

    @PostMapping("/eliminar-clientes/{id}")
    public RedirectView eliminarClientes(@PathVariable String id, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            clienteServicio.bajarCliente(id);
            Cliente cliente = clienteServicio.buscarPorId(id);
            attributes.addFlashAttribute("exito-name", "El cliente ha sido " + ((cliente.getAlta()) ? "habilitado " : "deshabilitado ") + "correctamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/clientes");
    }

}
