package fantaParcoBack.controller;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.entity.FantaEightBlack;
import fantaParcoBack.entity.FantaParco;
import fantaParcoBack.repository.EightBlackRepository;
import fantaParcoBack.repository.FantaEightBlackRepository;
import fantaParcoBack.repository.FantaParcoRepository;
import fantaParcoBack.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${stripe.webhook}")
    private String webhookSecret;

    @Autowired
    private EightBlackRepository eightBlackRepository;

    @Autowired
    private FantaParcoRepository fantaParcoRepository;

    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    @Autowired
    private EmailService emailService;

    @Value("${path.to.logo}") // Aggiungi questo per recuperare il percorso del logo
    private String logoPath; // Assicurati che sia il percorso corretto

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {

        try {
            Event event = Webhook.constructEvent(payload, signature, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();

                // Estrai i metadata
                String firstName = session.getMetadata().get("first_name");
                String lastName = session.getMetadata().get("last_name");
                String email = session.getMetadata().get("email");
                String email2 = session.getMetadata().get("email2");
                String cellphone = session.getMetadata().get("cellphone");
                String birthDate = session.getMetadata().get("birth_date");
                String location = session.getMetadata().get("location");
                String productName = session.getMetadata().get("product_name");
                Long amount = Long.valueOf(session.getMetadata().get("amount"));

                String htmlContent;

                // Gestisci i dati in base alla presenza di email ed email2
                if (email != null && !email.isEmpty() && email2 != null && !email2.isEmpty()) {
                    // Salva in FantaEightBlack
                    FantaEightBlack fantaEightBlack = new FantaEightBlack();
                    fantaEightBlack.setNome(firstName);
                    fantaEightBlack.setCognome(lastName);
                    fantaEightBlack.setFantaCalcio(email);
                    fantaEightBlack.setSkillBol(email2);
                    fantaEightBlack.setCellulare(cellphone);
                    fantaEightBlack.setDataDiNascita(birthDate);
                    fantaEightBlack.setLuogoDiNascita(location);
                    fantaEightBlack.setTotaleSpeso(amount);
                    fantaEightBlackRepository.save(fantaEightBlack);

                    // Crea contenuto HTML per FantaEightBlack
                    htmlContent = "<html><body style='text-align: center;'>"
                            + "<img src='cid:logo' alt='Logo' style='width:200px;height:auto; margin: auto;'/>"
                            + "<h2>Grazie " + firstName + "!</h2>"
                            + "<h3>Conferma Acquisto FantaParco + BlackEight</h3>"
                            + "<p>Hai speso: " + amount / 100 + " euro.</p>"
                            + "<p>Pacchetto acquistato: " + productName + "</p>"
                            + "<h4>Dati inseriti:</h4>"
                            + "<p><strong>Nome:</strong> " + firstName + "</p>"
                            + "<p><strong>Cognome:</strong> " + lastName + "</p>"
                            + "<p><strong>Email Fantacalcio:</strong> " + email + "</p>"
                            + "<p><strong>Email SkillBol:</strong> " + email2 + "</p>"
                            + "<p><strong>Cellulare:</strong> " + cellphone + "</p>"
                            + "<p><strong>Data di Nascita:</strong> " + birthDate + "</p>"
                            + "<p><strong>Luogo di Nascita:</strong> " + location + "</p>"
                            + "</body></html>";

                    // Invia email di conferma
                    emailService.sendHtmlEmail(email, "Conferma Acquisto FantaParco + BlackEight", htmlContent, logoPath);

                } else if (email != null && !email.isEmpty()) {
                    // Salva in FantaParco
                    FantaParco fantaParco = new FantaParco();
                    fantaParco.setNome(firstName);
                    fantaParco.setCognome(lastName);
                    fantaParco.setFantaCalcio(email);
                    fantaParco.setCellulare(cellphone);
                    fantaParco.setDataDiNascita(birthDate);
                    fantaParco.setLuogoDiNascita(location);
                    fantaParco.setTotaleSpeso(amount);
                    fantaParcoRepository.save(fantaParco);

                    // Crea contenuto HTML per FantaParco
                    htmlContent = "<html><body style='text-align: center;'>"
                            + "<img src='cid:logo' alt='Logo' style='width:200px;height:auto; margin: auto;'/>"
                            + "<h2>Grazie " + firstName + "!</h2>"
                            + "<h3>Conferma Acquisto FantaParco</h3>"
                            + "<p>Hai speso: " + amount / 100 + " euro.</p>"
                            + "<p>Pacchetto acquistato: " + productName + "</p>"
                            + "<h4>Dati inseriti:</h4>"
                            + "<p><strong>Nome:</strong> " + firstName + "</p>"
                            + "<p><strong>Cognome:</strong> " + lastName + "</p>"
                            + "<p><strong>Email Fantacalcio:</strong> " + email + "</p>"
                            + "<p><strong>Cellulare:</strong> " + cellphone + "</p>"
                            + "<p><strong>Data di Nascita:</strong> " + birthDate + "</p>"
                            + "<p><strong>Luogo di Nascita:</strong> " + location + "</p>"
                            + "</body></html>";

                    // Invia email di conferma
                    emailService.sendHtmlEmail(email, "Conferma Acquisto FantaParco", htmlContent, logoPath);
                } else if (email2 != null && !email2.isEmpty()) {
                    // Salva in EightBlack
                    EightBlack eightBlack = new EightBlack();
                    eightBlack.setNome(firstName);
                    eightBlack.setCognome(lastName);
                    eightBlack.setSkillBol(email2);
                    eightBlack.setTotaleSpeso(amount);
                    eightBlackRepository.save(eightBlack);

                    // Crea contenuto HTML per EightBlack
                    htmlContent = "<html><body style='text-align: center;'>"
                            + "<img src='cid:logo' alt='Logo' style='width:200px;height:auto; margin: auto;'/>"
                            + "<h2>Grazie " + firstName + "!</h2>"
                            + "<h3>Conferma Acquisto BlackEight</h3>"
                            + "<p>Hai speso: " + amount / 100 + " euro.</p>"
                            + "<p>Pacchetto acquistato: " + productName + "</p>"
                            + "<h4>Dati inseriti:</h4>"
                            + "<p><strong>Nome:</strong> " + firstName + "</p>"
                            + "<p><strong>Cognome:</strong> " + lastName + "</p>"
                            + "<p><strong>Email SkillBol:</strong> " + email2 + "</p>"
                            + "</body></html>";

                    // Invia email di conferma
                    emailService.sendHtmlEmail(email2, "Conferma Acquisto BlackEight", htmlContent, logoPath);
                }
            }
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Email Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Webhook Error: " + e.getMessage());
        }
        return ResponseEntity.ok("Success");
    }
}
