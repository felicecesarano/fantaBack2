package fantaParcoBack.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // Costruttore
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF (necessario in caso di utilizzo di JWT)
                .authorizeRequests()
                // Endpoint pubblici senza autenticazione JWT
                .requestMatchers("/api/eight-black/**", "/api/fanta-eight-black/**",
                        "/api/fanta-parco/**", "/api/login", "/api/check-email/**")
                .permitAll() // Consenti accesso senza autenticazione JWT a questi endpoint
                // Endpoint protetti che richiedono l'autenticazione JWT
                .requestMatchers("/table/**", "/api/clients/**", "/api/eight-black/clients",
                        "/api/eight-black/search", "/api/fanta-eight-black/clients",
                        "/api/fanta-eight-black/search", "/api/fanta-parco/clients",
                        "/api/fanta-parco/search")
                .authenticated() // Richiedono autenticazione JWT
                .anyRequest().permitAll(); // Altri endpoint sono pubblici, se necessario

        // Aggiungi il filtro JWT prima di UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configura l'AuthenticationManager per la gestione dei login
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}
