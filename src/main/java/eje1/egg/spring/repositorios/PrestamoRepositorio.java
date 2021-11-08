
package eje1.egg.spring.repositorios;

import eje1.egg.spring.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id = :id")
    public List<Prestamo> prestamosClienteId(@Param("id") String id) ;
    
    @Query("SELECT p FROM Prestamo p WHERE p.alta = :alta")
    public List<Prestamo> findAll(@Param("alta") Boolean alta);
}
