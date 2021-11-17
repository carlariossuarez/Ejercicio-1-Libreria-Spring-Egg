package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Rol;
import eje1.egg.spring.entidades.Usuario;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.UsuarioRepositorio;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private final String MENSAJE_USERNAME = "No existe un usuario registrado con el correo %s";

    @Transactional
    public Usuario crear(String correo, String clave, Rol rol) throws Exception {
        try {
            if (usuarioRepositorio.existsByCorreo(correo)) {
                throw new ErrorServicio("Ya existe un usuario asociado con el correo ingresado");
            }

            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setClave(encoder.encode(clave));
            usuario.setAlta(true);
            usuario.setRol(rol);

            usuarioRepositorio.save(usuario);
            return usuario;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }
    
    @Transactional
    public Usuario crear(String correo, String clave, String valid, Rol rol) throws Exception {
        try {
            if (usuarioRepositorio.existsByCorreo(correo)) {
                throw new ErrorServicio("Ya existe un usuario asociado con el correo ingresado");
            }
            validarIngreso(valid);
            Usuario usuario = new Usuario();
            usuario.setCorreo(correo);
            usuario.setClave(encoder.encode(clave));
            usuario.setAlta(true);
            
            usuario.setRol(rol);

            usuarioRepositorio.save(usuario);
            return usuario;
        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void modificar(String id, String correo, String clave) throws Exception, ErrorServicio {
        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setCorreo(correo);
                usuario.setClave(clave);
                usuario.setClave(encoder.encode(clave));
                usuarioRepositorio.save(usuario);
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
    public void bajarUsuario(String id)throws Exception, ErrorServicio{
        try {
            Optional <Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            usuario.setAlta(usuario.getAlta()? false:true);
            
            usuarioRepositorio.save(usuario);
        }else{
            throw new ErrorServicio("No se encontró el ID");
        }
        }catch(ErrorServicio ex){
            throw ex;
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuarios() throws Exception {
        try {
            return usuarioRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener usuarios");
        }
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id) throws Exception{
        
        try {
            Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id);
        return usuarioOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar por ID");
        }
        
     }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Correo no se encuentra registrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        return new User(usuario.getCorreo(), usuario.getClave(), Collections.singletonList(authority));
    }
    
    public void validarIngreso(String valid)throws ErrorServicio, Exception{
        if(!valid.equalsIgnoreCase("admin123") ){
            throw new ErrorServicio("La contraseña ingresada es incorrecta, solicite ayuda a soporte técnico");
        }
    }

}
