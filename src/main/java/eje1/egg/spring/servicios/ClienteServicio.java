package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Cliente;
import eje1.egg.spring.entidades.Usuario;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.ClienteRepositorio;
import eje1.egg.spring.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearCliente(Long documento, String nombre, String apellido, String telefono, String idUsuario) throws ErrorServicio, Exception {

        try {
            Cliente cliente = new Cliente();
            validar(documento, nombre, apellido, telefono);

            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                cliente.setDocumento(documento);
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setTelefono(telefono);
                cliente.setAlta(true);
                cliente.setUsuario(usuario);
                clienteRepositorio.save(cliente);
            } else {
                throw new ErrorServicio("No se encontró el ID");
            }

        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void crearCliente(Usuario usuario) throws ErrorServicio, Exception {
        try {
            validar(usuario);
            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            cliente.setAlta(true);
            clienteRepositorio.save(cliente);
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void modificarCliente(String id, Long documento, String nombre, String apellido, String telefono) throws ErrorServicio, Exception {
        try {
            validar(documento, nombre, apellido, telefono);
            Optional<Cliente> respuesta = clienteRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Cliente cliente = respuesta.get();
                cliente.setDocumento(documento);
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setTelefono(telefono);

                clienteRepositorio.save(cliente);
            } else {
                throw new ErrorServicio("No se encontró el ID");
            }

        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void bajarCliente(String id) throws Exception, ErrorServicio {
        try {
            Optional<Cliente> respuesta = clienteRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Cliente cliente = respuesta.get();
                cliente.setAlta(cliente.getAlta() ? false : true);

                clienteRepositorio.save(cliente);
            } else {
                throw new ErrorServicio("No se encontró el ID");
            }

        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientes() throws Exception {
        try {
            return clienteRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener clientes");
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(String id) throws Exception {
        try {
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(id);
            return clienteOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar ID de cliente");
        }
    }

    public void validar(Long documento, String nombre, String apellido, String telefono) throws ErrorServicio, Exception {
        if (documento == null) {
            throw new ErrorServicio("El documento no puede ser nulo");
        }
        if (validarDni(Long.toString(documento)) == false) {
            throw new ErrorServicio("El documento puede contener de 7 a 9 digitos");
        }
        if (nombre == null) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        } else if (nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        } else if (nombre.length() <= 1) {
            throw new ErrorServicio("El nombre no puede tener una sola letra");
        }
        if (apellido == null) {
            throw new ErrorServicio("El apellido no puede ser nulo");
        } else if (apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido no puede estar vacio");
        } else if (apellido.length() <= 1) {
            throw new ErrorServicio("El apellido no puede tener una sola letra");
        }
        if (telefono == null) {
            throw new ErrorServicio("El telefono no puede ser nulo");
        } else if (telefono.trim().isEmpty()) {
            throw new ErrorServicio("El telefono no puede estar vacío");
        }

    }

    private Boolean validarDni(String dni) {
        return dni.matches("^[0-9]{7,9}$");
    }
    
    public void validar(Usuario usuario)throws ErrorServicio, Exception{
        try {
            if (usuario == null){
            throw new ErrorServicio("El usuario no puede ser nulo");
        }
        } catch (ErrorServicio ex) {
            throw ex;
        }catch(Exception e){
            throw e;
        }
        
    }

}
