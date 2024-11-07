package fantaParcoBack.entity;

public class Login {
    private String username;
    private String password;

    // Costruttore con 2 parametri
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter e setter per username e password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
