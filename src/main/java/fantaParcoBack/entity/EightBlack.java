package fantaParcoBack.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "eight_black")
public class EightBlack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String skillBol;
    private Long totaleSpeso; // Aggiunto campo

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getSkillBol() {
        return skillBol;
    }

    public void setSkillBol(String skillBol) {
        this.skillBol = skillBol;
    }

    public Long getTotaleSpeso() {
        return totaleSpeso; // Getter per totaleSpeso
    }

    public void setTotaleSpeso(Long totaleSpeso) {
        this.totaleSpeso = totaleSpeso; // Setter per totaleSpeso
    }
}
