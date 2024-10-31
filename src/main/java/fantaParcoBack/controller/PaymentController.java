package fantaParcoBack.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import fantaParcoBack.dto.PaymentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class PaymentController {

    @PostMapping("/create-checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody PaymentDTO paymentDTO) {
        try {
            // Crea la sessione di checkout di Stripe
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/success?session_id{CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:4200/cancel")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("eur")
                                    .setUnitAmount(paymentDTO.getAmount())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(paymentDTO.getProductName())
                                            .build())
                                    .build())
                            .build());

            // Aggiungi entrambi i campi email ed email2 nei metadati
            builder.putMetadata("email", paymentDTO.getEmail());   // Email per FantaParco
            builder.putMetadata("email2", paymentDTO.getEmail2()); // Email per SkillBol

            // Aggiungi gli altri metadati
            builder.putMetadata("first_name", paymentDTO.getFirstName())
                    .putMetadata("last_name", paymentDTO.getLastName())
                    .putMetadata("birth_date", paymentDTO.getBirthDate())
                    .putMetadata("location", paymentDTO.getLocation())
                    .putMetadata("cellphone", paymentDTO.getCellphone())
                    .putMetadata("amount", String.valueOf(paymentDTO.getAmount()))
            .putMetadata("product_name", paymentDTO.getProductName());


            SessionCreateParams params = builder.build();
            Session session = Session.create(params);

            return ResponseEntity.ok(Map.of("id", session.getId(), "url", session.getUrl()));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
