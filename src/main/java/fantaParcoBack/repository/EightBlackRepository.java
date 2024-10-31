package fantaParcoBack.repository;

import fantaParcoBack.entity.EightBlack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EightBlackRepository extends JpaRepository<EightBlack, Long> {
    boolean existsBySkillBol(String skillBol);

    @Query("SELECT e FROM EightBlack e WHERE LOWER(e.nome) LIKE LOWER(CONCAT(:prefix, '%')) OR LOWER(e.cognome) LIKE LOWER(CONCAT(:prefix, '%'))")
    List<EightBlack> searchByPrefix(@Param("prefix") String prefix);
}