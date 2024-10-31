package fantaParcoBack.service;

import fantaParcoBack.entity.FantaEightBlack;

import fantaParcoBack.repository.FantaEightBlackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FantaEightBlackService {
    @Autowired
    private FantaEightBlackRepository fantaEightBlackRepository;

    public List<FantaEightBlack> getAllClients() {
    return fantaEightBlackRepository.findAll();
    }


    public List<FantaEightBlack> searchClientsByPrefix(String prefix) {
        return fantaEightBlackRepository.searchByPrefix(prefix);
    }
}
