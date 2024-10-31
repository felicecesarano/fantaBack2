package fantaParcoBack.service;


import fantaParcoBack.entity.FantaParco;

import fantaParcoBack.repository.FantaParcoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FantaParcoService {
    @Autowired
    private FantaParcoRepository fantaParcoRepository;

    public List<FantaParco> getAllClients() {
        return fantaParcoRepository.findAll();
    }


    public List<FantaParco> searchClientsByPrefix(String prefix) {
        return fantaParcoRepository.searchByPrefix(prefix);
    }
}
