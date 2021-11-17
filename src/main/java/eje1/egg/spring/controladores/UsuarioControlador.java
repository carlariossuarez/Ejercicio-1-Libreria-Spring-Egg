
package eje1.egg.spring.controladores;

import eje1.egg.spring.entidades.Rol;
import eje1.egg.spring.entidades.Usuario;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.servicios.ClienteServicio;
import eje1.egg.spring.servicios.RolServicio;
import eje1.egg.spring.servicios.UsuarioServicio;
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
@RequestMapping("/usuarios")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RolServicio rolServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping
    public ModelAndView mostrarUsuarios(HttpServletRequest request) throws Exception, ErrorServicio {
        ModelAndView mav = new ModelAndView("usuarios");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito-name"));
            mav.addObject("error", flashMap.get("error-name"));
        }
        mav.addObject("clientes", usuarioServicio.obtenerUsuarios());

        return mav;
    }
    
    @GetMapping("/crear-usuarios")
    public ModelAndView crearClientes() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("clientes-formulario");
            mav.addObject("cliente", new Usuario());
            mav.addObject("usuario", rolServicio.buscarTodos());
            mav.addObject("title", "Crear Usuario");
            mav.addObject("action", "guardar-usuarios");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
    
    @GetMapping("/crear-admin")
    public ModelAndView crearAdmin() throws Exception {
        try {
            ModelAndView mav = new ModelAndView("clientes-formulario");
            mav.addObject("cliente", new Usuario());
            mav.addObject("usuario", rolServicio.buscarTodos());
            mav.addObject("title", "Crear Usuario");
            mav.addObject("action", "guardar-admin");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
    
    @GetMapping("/editar-usuarios/{id}")
    public ModelAndView editarUsuarios(@PathVariable String id) throws Exception, ErrorServicio {
        try {
            ModelAndView mav = new ModelAndView("clientes-formulario");
            mav.addObject("cliente", usuarioServicio.buscarPorId(id));
            mav.addObject("title", "Editar Usuario");
            mav.addObject("action", "modificar-usuarios");
            return mav;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @PostMapping("/guardar-usuarios")
    public RedirectView guardarUsuarios(@RequestParam String correo, @RequestParam String clave
            , RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {

            clienteServicio.crearCliente(usuarioServicio.crear(correo, clave,rolServicio.buscarNombre("USUARIO")));
            attributes.addFlashAttribute("exito-name", "El usuario ha sido creado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/login");

    }
    @PostMapping("/guardar-admin")
    public RedirectView guardarUsuarios(@RequestParam String correo, @RequestParam String clave, @RequestParam String valid
            , RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
           
            clienteServicio.crearCliente(usuarioServicio.crear(correo, clave, valid, rolServicio.buscarNombre("ADMIN")));
            attributes.addFlashAttribute("exito-name", "El usuario ha sido creado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/login");

    }
    
    @PostMapping("/modificar-usuarios")
    public RedirectView modificarUsuarios(@RequestParam String id, @RequestParam String correo, @RequestParam String clave
            , RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            usuarioServicio.modificar(id, correo, clave);
            attributes.addFlashAttribute("exito-name", "El usuario ha sido modificado exitosamente");

        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }
        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/eliminar-usuarios/{id}")
    public RedirectView eliminarUsuarios(@PathVariable String id, RedirectAttributes attributes) throws Exception, ErrorServicio {
        try {
            usuarioServicio.bajarUsuario(id);
            Usuario usuario = usuarioServicio.buscarPorId(id);
            attributes.addFlashAttribute("exito-name", "El usuario ha sido " + ((usuario.getAlta()) ? "habilitado " : "deshabilitado ") + "correctamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name", e.getMessage());
        }

        return new RedirectView("/usuarios");
    }
    
    
    
    
}
