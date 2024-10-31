package fantaParcoBack.service;

import fantaParcoBack.entity.EightBlack;
import fantaParcoBack.repository.EightBlackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EightBlackService {

    @Autowired
    private EightBlackRepository eightBlackRepository;

    public List<EightBlack> getAllClients() {
        return eightBlackRepository.findAll();
    }


    public List<EightBlack> searchClientsByPrefix(String prefix) {
        return eightBlackRepository.searchByPrefix(prefix);
    }
}
