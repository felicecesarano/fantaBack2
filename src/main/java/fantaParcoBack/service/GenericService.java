package fantaParcoBack.service;

import org.springframework.data.jpa.repository.JpaRepository;

public class GenericService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public void deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Entità con ID " + id + " non trovata.");
        }
    }
}
