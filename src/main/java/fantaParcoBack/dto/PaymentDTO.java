package fantaParcoBack.dto;

public class PaymentDTO {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String location;
    private String cellphone;
    private String email;
    private String email2;
    private long amount;
    private String productName; // Nuovo campo

    // Costruttore
    public PaymentDTO() {}

    public PaymentDTO(String firstName, String lastName, String birthDate, String location, String cellphone, String email, String email2, long amount, String productName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.location = location;
        this.cellphone = cellphone;
        this.email = email;
        this.email2 = email2;
        this.amount = amount;
        this.productName = productName;
    }

    // Getter e Setter per productName e altri campi
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter e Setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2; // Corretto: restituisce email2
    }

    public void setEmail2(String email2) {
        this.email2 = email2; // Corretto: imposta email2
    }

    public long getAmount() { // Aggiungi il getter per amount
        return amount;
    }

    public void setAmount(long amount) { // Aggiungi il setter per amount
        this.amount = amount;
    }
}
