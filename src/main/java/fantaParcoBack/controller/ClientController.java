package fantaParcoBack.controller;

import fantaParcoBack.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Object>> getAllClients() {
        List<Object> allClients = clientService.getAllClientsCombined();
        return ResponseEntity.ok(allClients);
    } // Endpoint per cercare i clienti per prefisso
    @GetMapping("/search")
    public ResponseEntity<List<Object>> searchClientsByPrefix(String prefix) {
        List<Object> matchingClients = clientService.searchClientsByPrefix(prefix);
        return ResponseEntity.ok(matchingClients);
    }}
