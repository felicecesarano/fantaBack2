package fantaParcoBack.entity;

public class Login {
    private String username;
    private String password;

    // Costruttore vuoto
    public Login() {}

    // Costruttore con parametri
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter e Setter per username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter e Setter per password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
