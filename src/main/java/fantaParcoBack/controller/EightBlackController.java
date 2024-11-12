package fantaParcoBack.controller;

import fantaParcoBack.dto.PaymentDTO;
import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.repository.EightBlackRepository;
import fantaParcoBack.repository.FantaEightBlackRepository;
import fantaParcoBack.service.EightBlackService;
import fantaParcoBack.service.GenericService;
import fantaParcoBack.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eight-black")
public class EightBlackController {

    @Autowired
    private EightBlackRepository eightBlackRepository;

    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private EightBlackService eightBlackService;

    private final GenericService<EightBlack, Long> genericService;

    @Autowired
    public EightBlackController(@Qualifier("eightBlackRepository") EightBlackRepository eightBlackRepository) {
        this.genericService = new GenericService<>(eightBlackRepository);
    }

    @DeleteMapping("/clients/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable Long clientId) {
        try {
            genericService.deleteById(clientId);
            return ResponseEntity.ok("Cliente eliminato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Errore: " + e.getMessage());
        }
    }
    // Endpoint per ottenere tutti i clienti
    @GetMapping("/clients")
    public ResponseEntity<List<EightBlack>> getAllClient() {
        List<EightBlack> clients = eightBlackService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Endpoint per cercare i clienti per nome e cognome
    @GetMapping("/search")
    public ResponseEntity<List<EightBlack>> searchClients(@RequestParam String prefix) {
        List<EightBlack> results = eightBlackService.searchClientsByPrefix(prefix);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean existsInEightBlack = eightBlackRepository.existsBySkillBol(email);
        boolean existsInFantaEightBlack = fantaEightBlackRepository.existsBySkillBol(email);

        return ResponseEntity.ok(existsInEightBlack || existsInFantaEightBlack);
    }

    @PostMapping("/process-payment")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentDTO paymentDTO) {
        // Crea metadata per Stripe
        Map<String, String> metadata = new HashMap<>();
        metadata.put("first_name", paymentDTO.getFirstName());
        metadata.put("last_name", paymentDTO.getLastName());
        metadata.put("email2", paymentDTO.getEmail2());
        metadata.put("amount", String.valueOf(paymentDTO.getAmount()));

        String paymentUrl;
        try {
            paymentUrl = stripeService.createCheckoutSession("eur", paymentDTO.getAmount(),
                    "http://fantaparcodeiprincipi-51ffd.web.app/success", "http://fantaparcodeiprincipi-51ffd.web.app/cancel", metadata).getUrl();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Impossibile creare la sessione di pagamento: " + e.getMessage()));
        }

        // Restituisci solo l'URL di pagamento
        Map<String, Object> response = new HashMap<>();
        response.put("url", paymentUrl);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentDTO paymentDTO) {
        EightBlack eightBlack = new EightBlack();
        eightBlack.setNome(paymentDTO.getFirstName());
        eightBlack.setCognome(paymentDTO.getLastName());
        eightBlack.setSkillBol(paymentDTO.getEmail2());
        eightBlack.setTotaleSpeso(paymentDTO.getAmount());

        eightBlackRepository.save(eightBlack);
        return ResponseEntity.ok("Dati salvati con successo");
    }
}
