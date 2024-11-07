package fantaParcoBack.service;

import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.entity.FantaEightBlack;
import fantaParcoBack.entity.FantaParco;
import fantaParcoBack.repository.EightBlackRepository;
import fantaParcoBack.repository.FantaEightBlackRepository;
import fantaParcoBack.repository.FantaParcoRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    @Autowired
    private EightBlackRepository eightBlackRepository;

    @Autowired
    private FantaParcoRepository fantaParcoRepository;

    // Metodo per ottenere tutti i clienti combinati
    public List<Object> getAllClientsCombined() {
        List<EightBlack> eightBlackClients = eightBlackRepository.findAll();
        List<FantaEightBlack> fantaEightBlackClients = fantaEightBlackRepository.findAll();
        List<FantaParco> fantaParcoClients = fantaParcoRepository.findAll();

        // Combina tutte le liste in una lista comune
        List<Object> allClients = new ArrayList<>();
        allClients.addAll(eightBlackClients);
        allClients.addAll(fantaEightBlackClients);
        allClients.addAll(fantaParcoClients);

        return allClients;
    }

    public List<Object> searchClientsByPrefix(String prefix) {
        List<EightBlack> eightBlackClients = eightBlackRepository.searchByPrefix(prefix);
        List<FantaEightBlack> fantaEightBlackClients = fantaEightBlackRepository.searchByPrefix(prefix);
        List<FantaParco> fantaParcoClients = fantaParcoRepository.searchByPrefix(prefix);

        // Combina i risultati in una lista comune
        List<Object> allMatchingClients = new ArrayList<>();
        allMatchingClients.addAll(eightBlackClients);
        allMatchingClients.addAll(fantaEightBlackClients);
        allMatchingClients.addAll(fantaParcoClients);

        return allMatchingClients;
    }
}
