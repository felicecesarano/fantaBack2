package fantaParcoBack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import fantaParcoBack.entity.Login;
import fantaParcoBack.entity.AuthResponse;
import fantaParcoBack.security.JwtTokenProvider;

import java.util.Arrays;
import java.util.List;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    // Lista di utenti fissi
    private static final List<Login> fixedAdmins = Arrays.asList(
            new Login("arpaia_nunzio@libero.it", "Allclient"),
            new Login("sperindeo_pasquale@gmail.com", "Allclient"),
            new Login("carfora_davide@gmail.com", "Allclient")
    );

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Metodo per fare login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login loginRequest) {
        // Verifica se le credenziali corrispondono a uno degli utenti fissi
        for (Login admin : fixedAdmins) {
            if (admin.getUsername().equals(loginRequest.getUsername()) && admin.getPassword().equals(loginRequest.getPassword())) {
                String token = jwtTokenProvider.createToken(loginRequest.getUsername());
                return ResponseEntity.ok(new AuthResponse(token)); // Restituisci il token
            }
        }

        // Se le credenziali non corrispondono a nessun admin, restituisci errore
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali errate");
    }
}