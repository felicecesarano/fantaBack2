package fantaParcoBack.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // Usa un valore di fallback se la variabile d'ambiente non è impostata
    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY") != null ? System.getenv("JWT_SECRET_KEY") : "defaultSecretKey";

    // Crea un token JWT
    public String createToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)  // Aggiungi i ruoli al payload
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 ora di validità
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Valida il token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token); // Validazione del token
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token scaduto");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token non supportato");
        } catch (Exception e) {
            System.out.println("Token non valido");
        }
        return false;
    }

    // Estrai il nome utente dal token JWT
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Restituisce il nome utente
    }

    // Estrai i ruoli dal token JWT
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class); // Restituisce la lista dei ruoli
    }

    // Estrai le autorità dal token JWT
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> roles = getRolesFromToken(token); // Estrai i ruoli dal token
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Aggiungi il prefisso "ROLE_" per seguire la convenzione di Spring Security
                .collect(Collectors.toList());
    }
}
