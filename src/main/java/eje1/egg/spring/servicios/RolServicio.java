
package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Rol;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.RolRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicio {
    
    @Autowired
    RolRepositorio rolRepositorio;
    
    @Transactional
    public void crear(String nombre){
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rolRepositorio.save(rol);
                
        
    }
    
    @Transactional(readOnly = true)
    public List <Rol> buscarTodos(){
        return rolRepositorio.findAll();
    }
    
    @Transactional(readOnly = true)
    public Rol buscarNombre(String nombre) throws Exception, ErrorServicio{
        try {
            Rol rol = rolRepositorio.findByNombre(nombre).orElseThrow(() -> new ErrorServicio("Rol no registrado"));
        return rol;
        } catch (ErrorServicio ex) {
            throw ex;
        }catch(Exception e){
           throw e; 
        }
        
    }
    
    
}
