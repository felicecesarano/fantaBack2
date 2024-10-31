package fantaParcoBack.repository;



import fantaParcoBack.entity.FantaParco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FantaParcoRepository extends JpaRepository<FantaParco, Long> {
    boolean existsByFantaCalcio(String email);

    @Query("SELECT e FROM FantaParco e WHERE LOWER(e.nome) LIKE LOWER(CONCAT(:prefix, '%')) OR LOWER(e.cognome) LIKE LOWER(CONCAT(:prefix, '%'))")
    List<FantaParco> searchByPrefix(@Param("prefix") String prefix);
}
