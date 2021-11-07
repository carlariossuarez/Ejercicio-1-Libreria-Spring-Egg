package eje1.egg.spring.servicios;

import eje1.egg.spring.entidades.Cliente;
import eje1.egg.spring.entidades.Libro;
import eje1.egg.spring.entidades.Prestamo;
import eje1.egg.spring.errores.ErrorServicio;
import eje1.egg.spring.repositorios.ClienteRepositorio;
import eje1.egg.spring.repositorios.LibroRepositorio;
import eje1.egg.spring.repositorios.PrestamoRepositorio;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public void crearPrestamo(LocalDate fechaDevolucion, String idLibro, String idCliente) throws Exception, ErrorServicio {

        try {
           
           
            
            Prestamo prestamo = new Prestamo();
            validar(fechaDevolucion);

            Optional<Libro> respuesta1 = libroRepositorio.findById(idLibro);
            if (respuesta1.isPresent()) {
                Libro librox = respuesta1.get();
                if (librox.getEjemplaresRestantes() < 1) {
                    throw new ErrorServicio("No se encuentran libros disponibles para realizar el prestamo");
                }
            } else {
                throw new ErrorServicio("No se encontró el ID del libro");
            }
                Optional<Cliente> respuesta2 = clienteRepositorio.findById(idCliente);
                if (respuesta2.isPresent()) {
                    
                  } else {
                    throw new ErrorServicio("No se encontró el ID del cliente");
                }
                Libro libro = libroRepositorio.findById(idLibro).get();
                Cliente cliente = clienteRepositorio.findById(idCliente).get();
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                
                prestamo.setCliente(cliente);
                prestamo.setLibro(libro);
                prestamo.setFechaPrestamo(LocalDate.now());
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setAlta(true);
                
                

                libroRepositorio.save(libro);
                prestamoRepositorio.save(prestamo);


        } catch (ErrorServicio ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void modificarPrestamo(String id, LocalDate fechaDevolucion, String idLibro, String idCliente) throws Exception, ErrorServicio {
        try {
            Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Prestamo prestamo = respuesta.get();
                prestamo.setFechaDevolucion(fechaDevolucion);

                Optional<Libro> respuesta1 = libroRepositorio.findById(id);
                if (respuesta1.isPresent()) {
                    Libro libro = respuesta1.get();
                    prestamo.setLibro(libro);

                } else {
                    throw new ErrorServicio("No se encontró el ID");
                }

                Optional<Cliente> respuesta2 = clienteRepositorio.findById(id);
                if (respuesta2.isPresent()) {
                    Cliente cliente = respuesta2.get();
                    prestamo.setCliente(cliente);
                } else {
                    throw new ErrorServicio("No se encontró el ID");
                }

                prestamoRepositorio.save(prestamo);
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
    public void bajaPrestamo(String id) throws Exception, ErrorServicio {
        try {
            Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Prestamo prestamo = respuesta.get();
                prestamo.setFechaDevolucion(LocalDate.now());
                prestamo.setAlta(prestamo.getAlta() ? false : true);

                prestamoRepositorio.save(prestamo);
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
    public List<Prestamo> obtenerPrestamos() throws Exception {
        try {
            return prestamoRepositorio.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener lista de prestamos");
        }
    }

    @Transactional(readOnly = true)
    public Prestamo buscarPorId(String id) throws Exception {
        try {
            Optional<Prestamo> prestamoOptional = prestamoRepositorio.findById(id);
            return prestamoOptional.orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar prestamo por ID");
        }
    }

    public void validar(LocalDate fechaDevolucion) throws ErrorServicio, Exception {
        if (fechaDevolucion.isBefore(LocalDate.now())){
            throw new ErrorServicio("La fecha de devolución no puede ser anterior a la fecha actual");
        }

    }

}
