package fantaParcoBack.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable()) // Disabilita CSRF (attenzione in produzione)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**").permitAll()
                                .requestMatchers("/api/eight-black/**").permitAll()
                                .requestMatchers("/api/fanta-eight-black/**").permitAll()
                                .requestMatchers("/api/fanta-parco/**").permitAll()
                                .anyRequest().authenticated() // Tutto il resto richiede autenticazione
                );

        return http.build();
    }
}

