package fantaParcoBack.entity;

public class AuthResponse {

    private String token;

    // Costruttore
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter e Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
