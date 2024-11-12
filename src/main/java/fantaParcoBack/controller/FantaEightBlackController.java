package fantaParcoBack.controller;

import fantaParcoBack.dto.FantaEightBlackDTO;
import fantaParcoBack.dto.PaymentDTO;
import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.entity.FantaEightBlack;
import fantaParcoBack.entity.FantaParco;
import fantaParcoBack.repository.EightBlackRepository;
import fantaParcoBack.repository.FantaEightBlackRepository;
import fantaParcoBack.repository.FantaParcoRepository;
import fantaParcoBack.service.FantaEightBlackService;
import fantaParcoBack.service.GenericService;
import fantaParcoBack.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fanta-eight-black")
public class FantaEightBlackController {

    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    @Autowired
    private EightBlackRepository eightBlackRepository;

    @Autowired
    private FantaParcoRepository fantaParcoRepository;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private FantaEightBlackService fantaEightBlackService;
    @Autowired
    private GenericService<EightBlack, Long> genericService;

    // Endpoint per eliminare un cliente (specifico per EightBlack)
    @DeleteMapping("/clients/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable Long clientId) {
        try {
            genericService.deleteById(clientId); // Chiamata al servizio generico per eliminare l'entità
            return ResponseEntity.ok("Cliente eliminato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Errore: " + e.getMessage());
        }
    }
    @GetMapping("/clients")
    public ResponseEntity<List<FantaEightBlack>> getAllClients() {
        List<FantaEightBlack> clients = fantaEightBlackService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FantaEightBlack>> searchClients(@RequestParam String prefix) {
        List<FantaEightBlack> results = fantaEightBlackService.searchClientsByPrefix(prefix);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email, String email2) {
        System.out.println("Controllo email: " + email); // Aggiungi un log per controllare l'email

        boolean existsInParco = fantaParcoRepository.existsByFantaCalcio(email);
        boolean existsInFantaEightBlack = fantaEightBlackRepository.existsByFantaCalcio(email);
        boolean existsInFantaEightBlack2 = fantaEightBlackRepository.existsBySkillBol(email2);
        boolean existsInEightBlack = eightBlackRepository.existsBySkillBol(email2);

        Map<String, Object> response = new HashMap<>();
        response.put("existsInParco", existsInParco);
        response.put("existsInEightBlack", existsInEightBlack);
        response.put("existsInFantaEightBlack", existsInFantaEightBlack);
        response.put("existsInFantaEightBlack2", existsInFantaEightBlack2);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/process-payment")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentDTO paymentDTO) {
        // Crea la sessione di checkout per ottenere l'URL di pagamento
        String paymentUrl;
        Map<String, String> metadata = new HashMap<>();
        metadata.put("first_name", paymentDTO.getFirstName());
        metadata.put("last_name", paymentDTO.getLastName());
        metadata.put("email", paymentDTO.getEmail());
        metadata.put("email2", paymentDTO.getEmail2());
        metadata.put("birth_date", paymentDTO.getBirthDate());
        metadata.put("location", paymentDTO.getLocation());
        metadata.put("cellphone", paymentDTO.getCellphone());
        metadata.put("amount", String.valueOf(paymentDTO.getAmount()));

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
    public ResponseEntity<FantaEightBlack> create(@RequestBody FantaEightBlackDTO fantaEightBlackDTO) {
        FantaEightBlack fantaEightBlack = new FantaEightBlack();
        fantaEightBlack.setNome(fantaEightBlackDTO.getNome());
        fantaEightBlack.setCognome(fantaEightBlackDTO.getCognome());
        fantaEightBlack.setDataDiNascita(fantaEightBlackDTO.getDataDiNascita());
        fantaEightBlack.setLuogoDiNascita(fantaEightBlackDTO.getLuogoDiNascita());
        fantaEightBlack.setCellulare(fantaEightBlackDTO.getCellulare());
        fantaEightBlack.setFantaCalcio(fantaEightBlackDTO.getFantaCalcio());
        fantaEightBlack.setSkillBol(fantaEightBlackDTO.getSkillBol());
        fantaEightBlack.setTotaleSpeso(7000L); // 70€ in centesimi

        FantaEightBlack saved = fantaEightBlackRepository.save(fantaEightBlack);
        return ResponseEntity.ok(saved);
    }
}
