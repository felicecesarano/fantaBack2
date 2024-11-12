package fantaParcoBack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GenericService<T, ID> {

    private final JpaRepository<T, ID> repository;

    // Usa il repository passato dal costruttore
    public GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    // Metodo generico per eliminare un'entità per ID
    public void deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Entità con ID " + id + " non trovata.");
        }
    }
}
