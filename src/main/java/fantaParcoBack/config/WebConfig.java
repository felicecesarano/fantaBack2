package fantaParcoBack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://fantaparcodeiprincipi-51ffd.web.app",
                        "https://fantaback2.onrender.com",
                        "https://uptimerobot.com") // Aggiungi uptimerobot.com
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // I metodi che permetti
                .allowedHeaders("*") // Permetti tutte le intestazioni
                .allowCredentials(false); // Non è necessario per UptimeRobot, quindi impostalo su false
    }
}

