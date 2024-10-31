package fantaParcoBack.entity;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class Error {
    private String message;
    private LocalDateTime date;
    private HttpStatus status;

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
