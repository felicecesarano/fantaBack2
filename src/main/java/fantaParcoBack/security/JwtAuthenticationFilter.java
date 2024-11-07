package fantaParcoBack.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.List;

@WebFilter("/*") // Applica il filtro a tutte le richieste
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Salta l'autenticazione per l'endpoint /api/login
        if ("/api/login".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrai il token JWT dall'header Authorization
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Rimuove il prefisso "Bearer "

            // Verifica la validità del token
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

                if (authorities.isEmpty()) {
                    // Logica di fallimento se le autorità sono vuote
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Accesso non autorizzato, ruoli mancanti o insufficienti.");
                    return;
                }

                // Crea un oggetto di autenticazione con le autorità estratte
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Imposta l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Token non valido o scaduto
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Token JWT non valido o scaduto.");
                return;
            }
        } else if (token == null || !token.startsWith("Bearer ")) {
            // Token mancante o formato non valido
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Token JWT mancante o con prefisso errato.");
            return;
        }

        // Continua con la catena di filtri
        filterChain.doFilter(request, response);
    }


}
