
package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Editorial;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void crearEditorial(String nombre) throws Exception, ErrorServicio{
        try {
            Editorial editorial = new Editorial();
        validar(nombre);
        
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        
        editorialRepositorio.save(editorial);
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    @Transactional
    public void modificarEditorial(String id, String nombre) throws Exception, ErrorServicio{
        try {
            validar(nombre);
        Optional <Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            
            editorialRepositorio.save(editorial);
        }else{
            throw new ErrorServicio("No se encontró el ID");
        }
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
        
    }
    
    @Transactional
    public void bajarEditorial(String id) throws Exception, ErrorServicio{
        try {
            Optional <Editorial> respuesta = editorialRepositorio.findById(id);
        if(respuesta.isPresent()){
            Editorial editorial = respuesta.get();
            editorial.setAlta(editorial.getAlta()? false:true);
            
            editorialRepositorio.save(editorial);
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
    public List<Editorial> obtenerEditorial() throws Exception{
        try {
            return editorialRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener Editoriales");
        }
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) throws Exception{
        
        try {
            Optional<Editorial> libroOptional = editorialRepositorio.findById(id);
        return libroOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar por ID");
        }
        
     }
    
    
    
    
    
    private void validar(String nombre) throws Exception, ErrorServicio {
        try {
            if (nombre == null || nombre.isEmpty() ) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        
    }
}
