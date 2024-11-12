package fantaParcoBack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GenericService<T, ID> {

    @Autowired
    private JpaRepository<T, ID> repository;

    // Metodo generico per eliminare un'entità per ID
    public void deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id); // Elimina l'entità con ID specificato
        } else {
            throw new RuntimeException("Entità con ID " + id + " non trovata.");
        }
    }
}
