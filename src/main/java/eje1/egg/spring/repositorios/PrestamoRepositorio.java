
package eje1.egg.spring.repositorios;

import eje1.egg.spring.entidades.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{
    
}
