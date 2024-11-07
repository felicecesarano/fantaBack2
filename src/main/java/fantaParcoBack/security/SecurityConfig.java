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

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF (necessario in caso di utilizzo di JWT)
                .authorizeRequests()
                .requestMatchers("/api/**").permitAll() // Permetti l'accesso pubblico alle rotte API
                .requestMatchers("/api/eight-black/**").permitAll() // Permetti l'accesso pubblico alle rotte di Eight Black
                .requestMatchers("/api/fanta-eight-black/**").permitAll() // Permetti l'accesso pubblico alle rotte di FantaEightBlack
                .requestMatchers("/api/fanta-parco/**").permitAll() // Permetti l'accesso pubblico alle rotte di FantaParco
                .requestMatchers("/api/login").permitAll() // Permetti l'accesso pubblico alla rotta di login
                .requestMatchers("/table/**").authenticated() // Proteggi la rotta /table
                .anyRequest().authenticated(); // Tutte le altre rotte richiedono autenticazione

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
