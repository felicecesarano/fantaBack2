package fantaParcoBack.repository;


import fantaParcoBack.entity.FantaEightBlack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FantaEightBlackRepository extends JpaRepository<FantaEightBlack, Long> {
    boolean existsByFantaCalcio(String email);
    boolean existsBySkillBol(String skillbol);

    @Query("SELECT e FROM FantaEightBlack e WHERE LOWER(e.nome) LIKE LOWER(CONCAT(:prefix, '%')) OR LOWER(e.cognome) LIKE LOWER(CONCAT(:prefix, '%'))")
    List<FantaEightBlack> searchByPrefix(@Param("prefix") String prefix);
}

