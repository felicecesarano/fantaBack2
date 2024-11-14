package fantaParcoBack.controller;

import fantaParcoBack.dto.PaymentDTO;
import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.entity.FantaParco;
import fantaParcoBack.repository.FantaEightBlackRepository;
import fantaParcoBack.repository.FantaParcoRepository;
import fantaParcoBack.service.EightBlackService;
import fantaParcoBack.service.FantaParcoService;
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
@RequestMapping("/api/fanta-parco")
public class FantaParcoController {

    @Autowired
    private FantaParcoRepository fantaParcoRepository;

    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private FantaParcoService fantaParcoService;

    private final GenericService<FantaParco, Long> genericService;

    @Autowired
    public FantaParcoController(FantaParcoRepository fantaParcoRepository) {
        this.genericService = new GenericService<>(fantaParcoRepository);
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
    public ResponseEntity<List<FantaParco>> getAllClients() {
        List<FantaParco> clients = fantaParcoService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FantaParco>> searchClients(@RequestParam String prefix) {
        // Log per verificare l'accesso
        System.out.println("Searching for clients with prefix: " + prefix);
        List<FantaParco> results = fantaParcoService.searchClientsByPrefix(prefix);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        boolean existsInParco = fantaParcoRepository.existsByFantaCalcio(email);
        boolean existsInEightBlack = fantaEightBlackRepository.existsByFantaCalcio(email);

        Map<String, Object> response = new HashMap<>();
        response.put("existsInParco", existsInParco);
        response.put("existsInEightBlack", existsInEightBlack);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/process-payment")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentDTO paymentDTO) {
        // Prepara metadata per Stripe
        Map<String, String> metadata = new HashMap<>();
        metadata.put("first_name", paymentDTO.getFirstName());
        metadata.put("last_name", paymentDTO.getLastName());
        metadata.put("email", paymentDTO.getEmail());
        metadata.put("birth_date", paymentDTO.getBirthDate());
        metadata.put("location", paymentDTO.getLocation());
        metadata.put("cellphone", paymentDTO.getCellphone());
        metadata.put("product_name", paymentDTO.getProductName());
        metadata.put("amount", String.valueOf(paymentDTO.getAmount()));

        String paymentUrl;
        try {
            paymentUrl = stripeService.createCheckoutSession("eur", paymentDTO.getAmount(),
                    "http://fantaparcodeiprincipi-51ffd.web.app/success", "http://fantaparcodeiprincipi-51ffd.web.app/cancel", metadata).getUrl();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Impossibile creare la sessione di pagamento: " + e.getMessage()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("url", paymentUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentDTO paymentDTO) {
        // Crea e salva l'oggetto FantaParco nel database
        FantaParco fantaParco = new FantaParco();
        fantaParco.setNome(paymentDTO.getFirstName());
        fantaParco.setCognome(paymentDTO.getLastName());
        fantaParco.setDataDiNascita(paymentDTO.getBirthDate());
        fantaParco.setLuogoDiNascita(paymentDTO.getLocation());
        fantaParco.setCellulare(paymentDTO.getCellphone());
        fantaParco.setFantaCalcio(paymentDTO.getEmail());
        fantaParco.setTotaleSpeso(paymentDTO.getAmount());

        // Salva nel database
        fantaParcoRepository.save(fantaParco);

        return ResponseEntity.ok("Dati salvati con successo");
    }

}
