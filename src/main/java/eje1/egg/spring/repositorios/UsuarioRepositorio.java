
package eje1.egg.spring.repositorios;

import eje1.egg.spring.entidades.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    Optional<Usuario> findByCorreo(String correo); 
    
    boolean existsByCorreo(String correo);
}
