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

    // Endpoint protetti
    private static final List<String> PROTECTED_URLS = List.of(
            "/api/eight-black/clients",
            "/api/eight-black/search",
            "/api/fanta-eight-black/clients",
            "/api/fanta-eight-black/search",
            "/api/fanta-parco/clients",
            "/api/fanta-parco/search",
            "/api/clients"
    );

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Verifica se l'endpoint è uno di quelli da proteggere
        if (isProtectedUrl(path)) {
            // Estrai il token JWT dall'header Authorization
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Rimuove il prefisso "Bearer "

                // Verifica la validità del token
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    List<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

                    if (authorities.isEmpty()) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.getWriter().write("Accesso non autorizzato, ruoli mancanti o insufficienti.");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Token JWT non valido o scaduto.");
                    return;
                }
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Token JWT mancante o con prefisso errato.");
                return;
            }
        }

        // Continua con la catena di filtri per altre richieste
        filterChain.doFilter(request, response);
    }

    // Verifica se l'URL è tra quelli protetti
    private boolean isProtectedUrl(String path) {
        return PROTECTED_URLS.stream().anyMatch(path::startsWith);
    }
}

