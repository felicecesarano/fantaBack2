package fantaParcoBack.dto;

public class FantaParcoDTO {
    private String nome;
    private String cognome;
    private String dataDiNascita;
    private String luogoDiNascita;
    private String cellulare;
    private String fantaCalcio;
    private Long totaleSpeso; // Aggiunto campo

    // Getter e Setter
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

    public String getDataDiNascita() {
        return dataDiNascita;
    }

    public void setDataDiNascita(String dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    public String getLuogoDiNascita() {
        return luogoDiNascita;
    }

    public void setLuogoDiNascita(String luogoDiNascita) {
        this.luogoDiNascita = luogoDiNascita;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public String getFantaCalcio() {
        return fantaCalcio;
    }

    public void setFantaCalcio(String fantaCalcio) {
        this.fantaCalcio = fantaCalcio;
    }

    public Long getTotaleSpeso() {
        return totaleSpeso; // Getter per totaleSpeso
    }

    public void setTotaleSpeso(Long totaleSpeso) {
        this.totaleSpeso = totaleSpeso; // Setter per totaleSpeso
    }
}
