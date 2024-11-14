package fantaParcoBack.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public Session createCheckoutSession(String currency, long amount, String successUrl, String cancelUrl, Map<String, String> metadata) throws StripeException {
        // Recupera il nome del prodotto dai metadati (se presente)
        String productName = metadata.getOrDefault("product_name", "Prodotto da pagare"); // Usa il valore del metadato "product_name"

        // Crea i parametri della sessione di checkout
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(amount)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(productName) // Usa il nome del prodotto recuperato dai metadati
                                                .build())
                                .build())
                        .setQuantity(1L)
                        .build())
                .putAllMetadata(metadata) // Aggiungi i metadati qui
                .build();

        // Crea e restituisci la sessione di checkout
        return Session.create(params);
    }
}
