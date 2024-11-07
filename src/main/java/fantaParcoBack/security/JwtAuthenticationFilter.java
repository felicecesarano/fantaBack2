package fantaParcoBack.security;

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
        // Estrai il token JWT dall'header Authorization
        String token = request.getHeader("Authorization");

        // Se il token è presente e valido
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Rimuove il prefisso "Bearer "

            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

                // Crea un oggetto di autenticazione con i ruoli
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Imposta l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua con la catena di filtri
        filterChain.doFilter(request, response);
    }
}
