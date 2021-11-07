package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Autor;
import eje1.egg.spring.entidades.Editorial;
import eje1.egg.spring.entidades.Libro;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.AutorRepositorio;
import eje1.egg.spring.repositorios.EditorialRepositorio;
import eje1.egg.spring.repositorios.LibroRepositorio;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, String idEditorial, String idAutor) throws Exception, ErrorServicio {
        try {
            Libro libro = new Libro();

            validar(isbn, titulo, anio, ejemplares);

            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresRestantes(ejemplares);
            libro.setEjemplaresPrestados(0);

            libro.setAlta(true);
            Optional<Editorial> respuesta1 = editorialRepositorio.findById(idEditorial);
            if (respuesta1.isPresent()) {
                Editorial editorial = respuesta1.get();
                libro.setEditorial(editorial);
            } else {
                throw new ErrorServicio("No se encontró el ID");
            }
            Optional<Autor> respuesta2 = autorRepositorio.findById(idAutor);
            if (respuesta1.isPresent()) {
                Autor autor = respuesta2.get();
                libro.setAutor(autor);
            } else {
                throw new ErrorServicio("No se encontró el ID");
            }

            libroRepositorio.save(libro);
        } catch (ErrorServicio ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional
    public void modificarLibro(String id, Long isbn, String titulo, Integer anio,Integer ejemplares, String idEditorial, String idAutor) throws Exception, ErrorServicio {
        try {
            validar(isbn, titulo, anio, ejemplares);
            Optional<Libro> respuesta = libroRepositorio.findById(id);

            if (respuesta.isPresent()) {
                Libro libro = respuesta.get();
                libro.setIsbn(isbn);
                libro.setTitulo(titulo);
                libro.setAnio(anio);
                libro.setEjemplares(ejemplares);

                Optional<Editorial> respuesta1 = editorialRepositorio.findById(idEditorial);
                if (respuesta1.isPresent()) {
                    Editorial editorial = respuesta1.get();
                    libro.setEditorial(editorial);
                } else {
                    throw new ErrorServicio("No se encontró el ID");
                }
                Optional<Autor> respuesta2 = autorRepositorio.findById(idAutor);
                if (respuesta1.isPresent()) {
                    Autor autor = respuesta2.get();
                    libro.setAutor(autor);
                } else {
                    throw new ErrorServicio("No se encontró el ID");
                }

                

                libroRepositorio.save(libro);
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
    public void bajaLibro(String id) throws Exception, ErrorServicio {
        try {
            Optional<Libro> respuesta = libroRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Libro libro = respuesta.get();
                libro.setAlta(libro.getAlta()? false:true);
                libroRepositorio.save(libro);
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
    public List<Libro> obtenerLibros() throws Exception {
        try {
            return libroRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener libros");
        }
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) throws Exception {
        try {
            Optional<Libro> libroOptional = libroRepositorio.findById(id);
            return libroOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar por ID");
        }

    }

    private void validar(Long isbn, String titulo, Integer anio, Integer ejemplares) throws ErrorServicio {
        
        Calendar anioActual = Calendar.getInstance();
        try {
            if (isbn == null) {
                throw new ErrorServicio("El ISBN no puede ser nulo");
            }
            if (validarIsbn(Long.toString(isbn)) == false) {
                throw new ErrorServicio("El ISBN debe contener con 13 digitos y comenzar con 978");
            }
            if (titulo == null) {
                throw new ErrorServicio("El titulo no puede ser nulo");
            }else if (titulo.trim().isEmpty()){
                throw new ErrorServicio("El titulo no puede estar vacio");
            }else if(titulo.length()<=1){
                throw new ErrorServicio("El titulo no puede tener una sola letra");
            }
            if (anio == null) {
                throw new ErrorServicio("El año no puede ser nulo");
            }else if(anio > anioActual.get(Calendar.YEAR)){
                throw new ErrorServicio("El año no puede ser mayor al año actual");
            }
            if (ejemplares == null) {
                throw new ErrorServicio("Los ejemplares no pueden ser nulos");
            }else if(ejemplares < 0){
                throw new ErrorServicio("Los ejemplares no pueden ser menores a 0");
            }

        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }

    }

    

    private Boolean validarIsbn(String isbn) {
        return isbn.matches("^(978)[0-9]{10}$");
    }
}
