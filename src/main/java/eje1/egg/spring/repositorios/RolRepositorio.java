
package eje1.egg.spring.repositorios;

import eje1.egg.spring.entidades.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, Integer> {
    
    public Optional<Rol> findByNombre(String nombre);
}
