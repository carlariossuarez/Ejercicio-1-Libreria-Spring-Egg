package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Autor;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws Exception, ErrorServicio {
        try {
            Autor autor = new Autor();
            validar(nombre);

            autor.setNombre(nombre);
            autor.setAlta(true);

            autorRepositorio.save(autor);
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void modificarAutor(String id, String nombre) throws Exception, ErrorServicio {
        try {
            
            validar(nombre);
            
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el ID");
        }
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        

    }

    @Transactional
    public void bajarAutor(String id) throws Exception, ErrorServicio {
        try {
            Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(autor.getAlta()? false:true);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el ID");
        }
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        

    }

    @Transactional(readOnly = true)
    public List<Autor> obtenerAutor() throws Exception {
        try {
            return autorRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("No se encontró el ID");
        }
    }

    @Transactional(readOnly = true)
    public Autor buscarPorId(String id) throws Exception  {
        try {
            Optional<Autor> libroOptional = autorRepositorio.findById(id);
        return libroOptional.orElse(null);
        
        } catch (Exception e) {
            throw new Exception ("Error al buscar por ID");
        }
        
    }

    

    private void validar(String nombre) throws Exception, ErrorServicio {
        try {
            if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        }catch(ErrorServicio ex){
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        

    }
}
