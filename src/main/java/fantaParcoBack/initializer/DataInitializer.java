package fantaParcoBack.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        // Logica di inizializzazione
        System.out.println("Applicazione avviata e connessione al database stabilita.");

        // Puoi anche aggiungere altre logiche qui, se necessario
    }
}
