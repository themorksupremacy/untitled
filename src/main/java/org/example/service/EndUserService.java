package org.example.service;

import org.example.dto.EndUserDTO;
import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndUserService {
    private final EndUserRepository endUserRepository;

    public EndUserService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    public List<EndUserDTO> getAllUsers() {
        return endUserRepository.findAll()
                .stream()
                .map(endUser -> new EndUserDTO(
                        endUser.getId(),
                        endUser.getUsername(),
                        endUser.getEmail()))
                .collect(Collectors.toList());
    }

    public EndUserDTO getUserByUsername(String username) {
        EndUser endUser = endUserRepository.findByUsername(username);
        return new EndUserDTO(endUser.getId(), endUser.getUsername(), endUser.getEmail());
    }

    public EndUser saveUser(EndUser endUser) {
        return endUserRepository.save(endUser);
    }

    public void deleteUser(Long id) {
        endUserRepository.deleteById(id);
    }
}
