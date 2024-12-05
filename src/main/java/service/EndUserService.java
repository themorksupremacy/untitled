package service;

import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndUserService {
    private final EndUserRepository endUserRepository;

    public EndUserService(EndUserRepository endUserRepository) {

        this.endUserRepository = endUserRepository;
    }

    public List<EndUser> getAllUsers() {

        return endUserRepository.findAll();
    }

    public EndUser getUserByUsername(String username) {

        return endUserRepository.findByUsername(username);
    }

    public EndUser saveUser(EndUser endUser) {

        return endUserRepository.save(endUser);
    }

    public void deleteUser(Long id) {

        endUserRepository.deleteById(id);
    }
}
